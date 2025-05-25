package umc.th.juinjang.api.pencil.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.api.apple.service.AppleService;
import umc.th.juinjang.api.apple.service.command.AppleTransactionVerifyCommand;
import umc.th.juinjang.api.pencil.controller.request.AppleIAPPurchaseRequest;
import umc.th.juinjang.api.pencil.service.response.AppleIAPPurchaseResponse;
import umc.th.juinjang.api.pencil.service.response.VerificationResult;
import umc.th.juinjang.api.pencilAccount.service.PencilAccountFinder;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredPencil;
import umc.th.juinjang.domain.pencil.purchased.model.PurchasedPencil;
import umc.th.juinjang.domain.pencil.purchased.model.TransactionStatus;

@Slf4j
@Service
@RequiredArgsConstructor
public class PencilCommandService {

	private final AppleService appleService;

	private final PurchasedPencilUpdater purchasedPencilUpdater;
	private final PurchasedPencilFinder purchasedPencilFinder;
	private final AcquiredPencilFinder acquiredPencilFinder;
	private final PencilAccountFinder pencilAccountFinder;

	@Transactional
	public Boolean markAcquiredPencilAsRead(Long acquiredPencilId) {
		AcquiredPencil acquiredPencil = acquiredPencilFinder.findById(acquiredPencilId);

		if (acquiredPencil == null) {
			throw new EntityNotFoundException("AcquiredPencil not found with id: " + acquiredPencilId);
		}

		acquiredPencil.updateIsReadAsTrue();
		return true;
	}

	@Transactional
	public AppleIAPPurchaseResponse processAppleIAPPurchase(AppleIAPPurchaseRequest request, Member member,
		LocalDateTime now) {
		String transactionId = request.getTransactionId();

		Optional<PurchasedPencil> existing = purchasedPencilFinder.findByTransactionIdAndMember(transactionId, member);

		if (existing.isEmpty()) {
			// 기존 트랜잭션이 없는 경우
			log.info("기존 트랜잭션이 없습니다. {}", transactionId);
			return validateAndCommitApplePurchase(request, member, now);
		}

		PurchasedPencil pencil = existing.get();
		TransactionStatus status = pencil.getTransactionStatus();

		if (status == TransactionStatus.SUCCESS) {
			// 트랜잭션이 정상적으로 성공된 기록이 있는 경우
			return AppleIAPPurchaseResponse.ofSuccess(transactionId);
		}

		PurchasedPencil newPencil = retryPurchasedPencil(pencil, member); // 실패 재시도 처리
		return AppleIAPPurchaseResponse.of(transactionId, newPencil.getTransactionStatus());
	}



	@Transactional
	public AppleIAPPurchaseResponse validateAndCommitApplePurchase(AppleIAPPurchaseRequest request, Member member, LocalDateTime now) {
		String transactionId = request.getTransactionId();

		VerificationResult verificationResult = appleService.verifyAppleTransaction(AppleTransactionVerifyCommand.fromRequest(request));

		if (VerificationResult.isSuccess(verificationResult)){
			// 성공 시, DB에 저장z
			handleSuccessfulApplePurchase(request, member, now);

			// TODO : 디스코드 알림 추가 필요
			// paymentEventPublisher.publishPaymentEvent(member,request.getPrice(), pencilAmount,TransactionStatus.SUCCESS);
			return AppleIAPPurchaseResponse.ofSuccess(transactionId);
		}else{
			// 실패 시, DB에 저장
			handleFailureApplePurchase(request, member, now);

			// TODO : 디스코드 알림 추가 필요
			// paymentEventPublisher.publishPaymentEvent(member,request.getPrice(), pencilAmount,TransactionStatus.VALIDATION_FAILED);
			return AppleIAPPurchaseResponse.ofValidationFailure(transactionId);
		}
	}

	@Transactional
	public void handleSuccessfulApplePurchase(AppleIAPPurchaseRequest request, Member member, LocalDateTime now) {
		String transactionId = request.getTransactionId();
		Long pencilAmount = request.getPencilQuantity();

		String title = createTitle(pencilAmount);
		purchasedPencilUpdater.save(PurchasedPencil.successOf(member, title, pencilAmount, request.getPrice(),
			request.getPlayTime(), transactionId, request.getAppAccountToken(), now));

		pencilAccountFinder.findByMemberWithLock(member).increasePurchasedBalance(pencilAmount);
	}

	@Transactional
	public void handleFailureApplePurchase(AppleIAPPurchaseRequest request, Member member, LocalDateTime now) {
		String transactionId = request.getTransactionId();
		Long pencilAmount = request.getPencilQuantity();

		String title = createTitle(pencilAmount);
		purchasedPencilUpdater.save(PurchasedPencil.failedDueToValidation(member, title, pencilAmount, request.getPrice(),
			request.getPlayTime(), transactionId, request.getAppAccountToken(), now));
	}

	private PurchasedPencil retryPurchasedPencil(PurchasedPencil pencil, Member member) {
		if ( pencil.getRetryCount() >= 3 ) { // 재시도 횟수가 3회 이상일 경우 실패로 처리
			return pencil;
		}

		AppleIAPPurchaseRequest retryRequest = AppleIAPPurchaseRequest.ofRetry(pencil);
		VerificationResult verificationResult = appleService.verifyAppleTransaction(
			AppleTransactionVerifyCommand.fromRequest(retryRequest)
		);

		if (VerificationResult.isSuccess(verificationResult)) {
			pencil.markAsSuccess();
			pencil.updateRetryCount(pencil.getRetryCount() + 1);

			pencilAccountFinder.findByMemberWithLock(member)
				.increasePurchasedBalance(pencil.getPurchaseQuantity());
		}

		return pencil;
	}


	private String createTitle(Long pencilAmount) {
		return String.format("연필 %d개 구매", pencilAmount);
	}

}
