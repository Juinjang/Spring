package umc.th.juinjang.api.pencil.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AppleIAPPurchaseResponse {

	private Long pencilQuantity;
	private String transactionId;

	@Builder
	private AppleIAPPurchaseResponse(Long pencilQuantity, String transactionId) {
		this.pencilQuantity = pencilQuantity;
		this.transactionId = transactionId;
	}

	public static AppleIAPPurchaseResponse of(String transactionId, Long pencilQuantity) {
		return AppleIAPPurchaseResponse.builder()
			.pencilQuantity(pencilQuantity)
			.transactionId(transactionId)
			.build();
	}
}
