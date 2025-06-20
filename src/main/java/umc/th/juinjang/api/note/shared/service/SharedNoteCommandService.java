package umc.th.juinjang.api.note.shared.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.hibernate.exception.LockAcquisitionException;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.cloud.vision.v1.Likelihood;

import jakarta.persistence.PessimisticLockException;
import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.limjang.service.NoteFinder;
import umc.th.juinjang.api.limjang.service.NoteUpdater;
import umc.th.juinjang.api.note.shared.controller.request.SharedNotePostRequest;
import umc.th.juinjang.api.pencil.service.AcquiredPencilUpdater;
import umc.th.juinjang.api.pencil.service.PurchasedPencilUpdater;
import umc.th.juinjang.api.pencil.service.UsedPencilFinder;
import umc.th.juinjang.api.pencil.service.UsedPencilUpdater;
import umc.th.juinjang.api.pencilAccount.service.PencilAccountFinder;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.SharedNoteHandler;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.note.shared.model.SharedNote;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredPencil;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredType;
import umc.th.juinjang.domain.pencil.purchased.model.PurchasedPencil;
import umc.th.juinjang.domain.pencil.used.model.UsedPencil;
import umc.th.juinjang.domain.pencil.used.model.Usedtype;
import umc.th.juinjang.domain.pencilaccount.model.PencilAccount;
import umc.th.juinjang.external.safeSearch.SafeSearchClient;

@Service
@RequiredArgsConstructor
public class SharedNoteCommandService {

	private final SharedNoteFinder sharedNoteFinder;
	private final UsedPencilUpdater usedPencilUpdater;
	private final UsedPencilFinder usedPencilFinder;
	private final AcquiredPencilUpdater acquiredPencilUpdater;
	private final PencilAccountFinder pencilAccountFinder;
	private final NoteFinder noteFinder;
	private final SharedNoteUpdater sharedNoteUpdater;
	private final SafeSearchClient safeSearchClient;
	private final NoteUpdater noteUpdater;
	private final PurchasedPencilUpdater purchasedPencilUpdater;

	@Transactional
	public void createSharedNotePurchase(Member buyer, Long sharedNoteId) {
		checkAlreadyPurchase(buyer, sharedNoteId);

		SharedNote sharedNote = sharedNoteFinder.getByIdWhereDeletedAtIsNull(sharedNoteId);
		Member seller = sharedNote.getMember();
		Long price = sharedNote.getPrice();

		try {
			PencilAccount buyerAccount = pencilAccountFinder.findByMemberWithLock(buyer);
			PencilAccount sellerAccount = pencilAccountFinder.findByMemberWithLock(seller);

			executePayment(buyer, buyerAccount, sellerAccount, price);

			usedPencilUpdater.save(createUsedPencil(buyer, sharedNoteId, sharedNote, buyerAccount));
			acquiredPencilUpdater.save(createAcquiredPencil(sharedNoteId, seller, price, AcquiredType.SOLD));

		} catch (CannotAcquireLockException | PessimisticLockException | LockAcquisitionException e) {
			throw new SharedNoteHandler(ErrorStatus.SHAREDNOTE_DEADLOCK);
		}
	}

	private void checkAlreadyPurchase(Member buyer, Long sharedNoteId) {
		if (usedPencilFinder.existsByMemberAndSharedNoteId(buyer, sharedNoteId)) {
			throw new SharedNoteHandler(ErrorStatus.SHAREDNOTE_CONFLICT);
		}
	}

	public void executePayment(Member buyer, PencilAccount buyerAccount, PencilAccount sellerAccount, Long price) {
		long acquiredUsed = Math.min(buyerAccount.getAcquiredBalance(), price);
		buyerAccount.decreaseAcquiredBalance(acquiredUsed);

		long unpaidPencil = price - acquiredUsed;
		if (unpaidPencil > 0) {
			if (buyerAccount.getPurchasedBalance() < unpaidPencil) {
				throw new SharedNoteHandler(ErrorStatus.SHAREDNOTE_NOT_ENOUGH_PENCIL);
			}
			consumePurchasedPencils(buyer, unpaidPencil);
			buyerAccount.decreasePurchasedBalance(unpaidPencil);
		}

		sellerAccount.increaseAcquiredBalance(price);
	}

	private AcquiredPencil createAcquiredPencil(Long sharedNoteId, Member seller, Long price, AcquiredType type) {
		return AcquiredPencil.create(seller, "", sharedNoteId, price, false, type);
	}

	private void consumePurchasedPencils(Member buyer, long unpaidPencil) {
		List<PurchasedPencil> purchasedPencils = purchasedPencilUpdater.findByMemberAndDeliverySuccessRemainQuantityGreaterThanOrderByCreatedAtAsc(
			buyer, 0L);

		long remainingToConsume = unpaidPencil;

		for (PurchasedPencil purchasedPencil : purchasedPencils) {
			if (remainingToConsume == 0)
				break;

			long available = purchasedPencil.getRemainQuantity();
			long toConsume = Math.min(available, remainingToConsume);

			purchasedPencil.decreaseRemainQuantity(toConsume); // remainQuantity -= toConsume
			remainingToConsume -= toConsume;
		}

		if (remainingToConsume > 0) {
			throw new SharedNoteHandler(ErrorStatus.SHAREDNOTE_NOT_ENOUGH_PENCIL);
		}
	}

	private UsedPencil createUsedPencil(Member member, Long sharedNoteId, SharedNote sharedNote,
		PencilAccount buyerAccount) {
		return UsedPencil.create(member, sharedNoteId, sharedNote.getPrice(), Usedtype.OWNED,
			sharedNote.getBuildingName(), buyerAccount.getTotalBalance());
	}

	@Transactional
	public void deleteSharedNote(Member member, Long sharedNoteId, LocalDateTime deletedAt) {
		SharedNote sharedNote = sharedNoteFinder.getBySharedNoteIdAndMemberAndDeletedAtIsNull(sharedNoteId, member);
		sharedNote.updateDeletedAt(Timestamp.valueOf(deletedAt));
	}

	@Transactional
	public void createSharedNote(Member member, Long noteId, SharedNotePostRequest request) {

		Limjang limjang = noteFinder.getNoteByIdWhereDeletedIsFalse(noteId);
		Optional<SharedNote> latestSharedNote = sharedNoteFinder.findLatestByLimjangId(noteId);

		// 이미 삭제되지 않은 공유글이 있으면 차단
		if (latestSharedNote.isPresent() && latestSharedNote.get().getDeletedAt() == null) {
			throw new SharedNoteHandler(ErrorStatus.SHAREDNOTE_ALREADY_EXISTS);
		}

		// 최초 공유라면 보상 지급
		boolean isFirstTimeShared = latestSharedNote.isEmpty();
		int reward = calculateReward(limjang, request);
		Integer rewardPencilCount = isFirstTimeShared ? reward : 0;
		Long price = calculatePrice(reward);

		// 공유 저장
		SharedNote sharedNote = SharedNote.toSharedNote(member, limjang, request, price);
		sharedNoteUpdater.save(sharedNote);

		// 보상 처리
		if (rewardPencilCount > 0) {
			applyReward(member, limjang, sharedNote.getSharedNoteId(), rewardPencilCount);
		}
	}

	private int calculateReward(Limjang limjang, SharedNotePostRequest request) {
		if (request.isImageShared() == Boolean.TRUE && !limjang.getImageList().isEmpty()) {
			validateImagesAreSafe(limjang);
			return 7;
		} else
			return 2;
	}

	private Long calculatePrice(int reward) {
		if (reward == 7)
			return 10L;
		else
			return 5L;
	}

	private void validateImagesAreSafe(Limjang limjang) {
		for (var image : limjang.getImageList()) {
			boolean safe = safeSearchClient.isSafeImage(
				image.getImageUrl(),
				Likelihood.UNLIKELY,  // adult
				Likelihood.POSSIBLE,  // spoof
				Likelihood.POSSIBLE,  // medical
				Likelihood.UNLIKELY,  // violence
				Likelihood.LIKELY     // racy
			);
			if (!safe) {
				throw new SharedNoteHandler(ErrorStatus.SHARED_NOT_ALLOWED);
			}
		}
	}

	private void applyReward(Member member, Limjang limjang, Long sharedNoteId, int rewardPencilCount) {
		limjang.updateRewardPencil(rewardPencilCount);
		noteUpdater.save(limjang);

		PencilAccount pencilAccount = pencilAccountFinder.findByMemberWithLock(member);
		pencilAccount.increaseAcquiredBalance(rewardPencilCount);

		acquiredPencilUpdater.save(
			createAcquiredPencil(sharedNoteId, member, (long)rewardPencilCount, AcquiredType.NOTE));
	}

}
