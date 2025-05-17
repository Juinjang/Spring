package umc.th.juinjang.api.pencil.service.response;

import lombok.Builder;
import lombok.Getter;
import umc.th.juinjang.domain.pencil.purchased.model.TransactionStatus;

@Getter
public class AppleIAPPurchaseResponse {

	private TransactionStatus status;
	private String transactionId;

	@Builder
	private AppleIAPPurchaseResponse(TransactionStatus status,String transactionId) {
		this.status = status;
		this.transactionId = transactionId;
	}

	public static AppleIAPPurchaseResponse of(String transactionId, TransactionStatus status) {
		return AppleIAPPurchaseResponse.builder()
			.transactionId(transactionId)
			.status(status)
			.build();
	}

	public static AppleIAPPurchaseResponse ofSuccess(String transactionId) {
		return AppleIAPPurchaseResponse.builder()
			.status(TransactionStatus.SUCCESS)
			.transactionId(transactionId)
			.build();
	}

	public static AppleIAPPurchaseResponse ofValidationFailure(String transactionId) {
		return AppleIAPPurchaseResponse.builder()
			.status(TransactionStatus.VALIDATION_FAILED)
			.transactionId(transactionId)
			.build();
	}
}
