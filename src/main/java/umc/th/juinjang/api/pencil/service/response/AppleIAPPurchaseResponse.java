package umc.th.juinjang.api.pencil.service.response;

import lombok.Builder;
import lombok.Getter;
import umc.th.juinjang.domain.pencil.purchased.model.TransactionStatus;

@Getter
public class AppleIAPPurchaseResponse {

	private final TransactionStatus status;
	private final String transactionId;
	private final Long purchaseQuantity;
	private final Long remainQuantity;

	@Builder
	private AppleIAPPurchaseResponse(
		TransactionStatus status,
		String transactionId,
		Long purchaseQuantity,
		Long remainQuantity
	) {
		this.status = status;
		this.transactionId = transactionId;
		this.purchaseQuantity = purchaseQuantity;
		this.remainQuantity = remainQuantity;
	}

	/**
	 * 일반적인 정적 팩토리: 모든 필드 수동 지정
	 */
	public static AppleIAPPurchaseResponse of(String transactionId, TransactionStatus status, Long purchaseQuantity,
		Long remainQuantity) {
		return AppleIAPPurchaseResponse.builder()
			.transactionId(transactionId)
			.status(status)
			.purchaseQuantity(purchaseQuantity)
			.remainQuantity(remainQuantity)
			.build();
	}

	/**
	 * 성공 응답용 팩토리
	 */
	public static AppleIAPPurchaseResponse ofSuccess(String transactionId, Long purchaseQuantity, Long remainQuantity) {
		return AppleIAPPurchaseResponse.builder()
			.status(TransactionStatus.SUCCESS)
			.transactionId(transactionId)
			.purchaseQuantity(purchaseQuantity)
			.remainQuantity(remainQuantity)
			.build();
	}

	/**
	 * 검증 실패 응답용 팩토리
	 */
	public static AppleIAPPurchaseResponse ofValidationFailure(String transactionId) {
		return AppleIAPPurchaseResponse.builder()
			.status(TransactionStatus.VALIDATION_FAILED)
			.transactionId(transactionId)
			.purchaseQuantity(0L)
			.remainQuantity(null)
			.build();
	}
}
