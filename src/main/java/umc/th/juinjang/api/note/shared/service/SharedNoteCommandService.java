package umc.th.juinjang.api.note.shared.service;

import org.hibernate.exception.LockAcquisitionException;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.PessimisticLockException;
import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.pencil.service.AcquiredPencilUpdater;
import umc.th.juinjang.api.pencil.service.UsedPencilFinder;
import umc.th.juinjang.api.pencil.service.UsedPencilUpdater;
import umc.th.juinjang.api.pencilAccount.service.PencilAccountFinder;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.SharedNoteHandler;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.note.shared.model.SharedNote;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredPencil;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredType;
import umc.th.juinjang.domain.pencil.used.model.UsedPencil;
import umc.th.juinjang.domain.pencil.used.model.Usedtype;
import umc.th.juinjang.domain.pencilaccount.model.PencilAccount;

@Service
@RequiredArgsConstructor
public class SharedNoteCommandService {

	private final SharedNoteFinder sharedNoteFinder;
	private final UsedPencilUpdater usedPencilUpdater;
	private final UsedPencilFinder usedPencilFinder;
	private final AcquiredPencilUpdater acquiredPencilUpdater;
	private final PencilAccountFinder pencilAccountFinder;

	@Transactional
	public void createSharedNotePurchase(Member buyer, Long sharedNoteId) {
		checkAlreadyPurchase(buyer, sharedNoteId);

		SharedNote sharedNote = sharedNoteFinder.findById(sharedNoteId);
		Member seller = sharedNote.getMember();
		Long price = sharedNote.getPrice();

		try {
			PencilAccount buyerAccount = pencilAccountFinder.findByMemberWithLock(buyer);
			PencilAccount sellerAccount = pencilAccountFinder.findByMemberWithLock(seller);

			executePayment(buyerAccount, sellerAccount, price);

			usedPencilUpdater.save(createUsedPencil(buyer, sharedNoteId, sharedNote, buyerAccount));
			acquiredPencilUpdater.save(createAcquiredPencil(sharedNoteId, seller, price));
		} catch (CannotAcquireLockException | PessimisticLockException | LockAcquisitionException e) {
			throw new SharedNoteHandler(ErrorStatus.SHAREDNOTE_DEADLOCK);
		}
	}

	private void checkAlreadyPurchase(Member buyer, Long sharedNoteId) {
		if (usedPencilFinder.existsByMemberAndSharedNoteId(buyer, sharedNoteId)) {
			throw new SharedNoteHandler(ErrorStatus.SHAREDNOTE_CONFLICT);
		}
	}

	public void executePayment(PencilAccount buyerAccount, PencilAccount sellerAccount, Long price) {
		long acquiredUsed = Math.min(buyerAccount.getAcquiredBalance(), price);
		buyerAccount.decreaseAcquiredBalance(acquiredUsed);

		long unpaidPencil = price - acquiredUsed;
		if (unpaidPencil > 0) {
			if (buyerAccount.getPurchasedBalance() < unpaidPencil) {
				throw new SharedNoteHandler(ErrorStatus.SHAREDNOTE_NOT_ENOUGH_PENCIL);
			}
			buyerAccount.decreasePurchasedBalance(unpaidPencil);
		}

		sellerAccount.increaseAcquiredBalance(price);
	}

	private AcquiredPencil createAcquiredPencil(Long sharedNoteId, Member seller, Long price) {
		return AcquiredPencil.create(seller, "", sharedNoteId, price, false, AcquiredType.SOLD);
	}

	private UsedPencil createUsedPencil(Member member, Long sharedNoteId, SharedNote sharedNote,
		PencilAccount buyerAccount) {
		return UsedPencil.create(member, sharedNoteId, sharedNote.getPrice(), Usedtype.OWNED,
			sharedNote.getBuildingName(), buyerAccount.getTotalBalance());
	}
}
