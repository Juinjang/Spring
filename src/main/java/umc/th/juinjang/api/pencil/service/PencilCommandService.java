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
import umc.th.juinjang.domain.pencilaccount.model.PencilAccount;
import umc.th.juinjang.event.publisher.PaymentEventPublisher;

@Slf4j
@Service
@RequiredArgsConstructor
public class PencilCommandService {

	private final AppleService appleService;

	private final PurchasedPencilUpdater purchasedPencilUpdater;
	private final PurchasedPencilFinder purchasedPencilFinder;
	private final AcquiredPencilFinder acquiredPencilFinder;
	private final PencilAccountFinder pencilAccountFinder;
	private final PaymentEventPublisher paymentEventPublisher;

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
		Long purchaseQuantity = request.getPencilQuantity();
		Optional<PurchasedPencil> existing = purchasedPencilFinder.findByTransactionIdAndMember(transactionId, member);

		if (existing.isEmpty()) {
			// 기존 트랜잭션이 없는 경우
			log.info("기존 트랜잭션이 없습니다. {}", transactionId);
			return validateAndCommitApplePurchase(request, member, now);
		}

		PurchasedPencil pencil = existing.get();
		TransactionStatus status = pencil.getTransactionStatus();
		PencilAccount buyer = pencilAccountFinder.findByMember(member);

		if (status == TransactionStatus.SUCCESS) {
			// 트랜잭션이 정상적으로 성공된 기록이 있는 경우
			return AppleIAPPurchaseResponse.ofSuccess(transactionId, purchaseQuantity, buyer.getTotalBalance());
		}

		PurchasedPencil newPencil = retryPurchasedPencil(request, pencil, member); // 실패 재시도 처리
		return AppleIAPPurchaseResponse.of(transactionId, newPencil.getTransactionStatus(), purchaseQuantity,
			buyer.getTotalBalance());
	}

	@Transactional
	public AppleIAPPurchaseResponse validateAndCommitApplePurchase(AppleIAPPurchaseRequest request, Member member,
		LocalDateTime now) {
		String transactionId = request.getTransactionId();

		VerificationResult verificationResult = appleService.verifyAppleTransaction(
			AppleTransactionVerifyCommand.fromRequest(request));

		if (VerificationResult.isSuccess(verificationResult)) {
			// 성공 시, DB에 저장
			handleSuccessfulApplePurchase(request, member, now);

			paymentEventPublisher.publishPaymentEvent(member, request.getPrice(), request.getPencilQuantity(),
				TransactionStatus.SUCCESS);
			PencilAccount buyer = pencilAccountFinder.findByMember(member);
			return AppleIAPPurchaseResponse.ofSuccess(transactionId, request.getPencilQuantity(),
				buyer.getTotalBalance());
		} else {
			// 실패 시, DB에 저장
			handleFailureApplePurchase(request, member, now);

			paymentEventPublisher.publishPaymentEvent(member, request.getPrice(), request.getPencilQuantity(),
				TransactionStatus.VALIDATION_FAILED);
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
		purchasedPencilUpdater.save(
			PurchasedPencil.failedDueToValidation(member, title, pencilAmount, request.getPrice(),
				request.getPlayTime(), transactionId, request.getAppAccountToken(), now));
	}

	@Transactional
	public PurchasedPencil retryPurchasedPencil(AppleIAPPurchaseRequest request, PurchasedPencil pencil,
		Member member) {
		if (pencil.getRetryCount() >= 3) { // 재시도 횟수가 3회 이상일 경우 실패로 처리
			return pencil;
		}

		VerificationResult verificationResult = appleService.verifyAppleTransaction(
			AppleTransactionVerifyCommand.fromRequest(request)
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

	@Transactional
	public void handleRefundPurchase(String transactionId) {
		PurchasedPencil pencil = purchasedPencilFinder.findByTransactionId(transactionId)
			.orElseThrow(
				() -> new EntityNotFoundException("PurchasedPencil not found with transactionId: " + transactionId));

		log.info("Refund processed for transactionId: {}", transactionId);

		pencil.markAsRefund();

		PencilAccount buyerAccount = pencilAccountFinder.findByMemberWithLock(pencil.getMember());
		executeRefund(buyerAccount, pencil.getPurchaseQuantity(), pencil.getPrice());

	}

	public void executeRefund(PencilAccount buyerAccount, long pencilQuantity, long price) {
		long purchasedToUse = Math.min(buyerAccount.getPurchasedBalance(), pencilQuantity);
		buyerAccount.decreasePurchasedBalance(purchasedToUse);

		long remaining = pencilQuantity - purchasedToUse;
		long acquiredToUse = Math.min(buyerAccount.getAcquiredBalance(), remaining);
		buyerAccount.decreaseAcquiredBalance(acquiredToUse);

		buyerAccount.increaseTotalRefundAmount(price);
		// 남은 수량이 0이 아닐 경우 로그 기록
		if (remaining - acquiredToUse > 0) {
			log.warn("Not enough balance to fully refund {} pencils. Refunded only {}.", pencilQuantity,
				(purchasedToUse + acquiredToUse));
		}
	}

}
