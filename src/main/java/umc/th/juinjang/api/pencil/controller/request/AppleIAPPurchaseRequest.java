package umc.th.juinjang.api.pencil.controller.request;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AppleIAPPurchaseRequest {
	private String transactionId;
	private UUID appAccountToken;
	private Long pencilQuantity;
	private Long price;
	private String productId;

	@Builder
	private AppleIAPPurchaseRequest(String transactionId, UUID appAccountToken, Long pencilQuantity, Long price,
		String productId) {
		this.transactionId = transactionId;
		this.appAccountToken = appAccountToken;
		this.pencilQuantity = pencilQuantity;
		this.price = price;
		this.productId = productId;
	}

	public static AppleIAPPurchaseRequest of(String transactionId, UUID appAccountToken, Long pencilQuantity,
		Long price,
		String productId) {
		return AppleIAPPurchaseRequest.builder()
			.transactionId(transactionId)
			.appAccountToken(appAccountToken)
			.pencilQuantity(pencilQuantity)
			.price(price)
			.productId(productId)
			.build();
	}
}
