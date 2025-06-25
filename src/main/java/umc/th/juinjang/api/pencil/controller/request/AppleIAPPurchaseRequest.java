package umc.th.juinjang.api.pencil.controller.request;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import umc.th.juinjang.domain.pencil.purchased.model.PurchasedPencil;

@Getter
public class AppleIAPPurchaseRequest {
	private String transactionId;
	private UUID appAccountToken;
	private Long pencilQuantity;
	private Long price;
	private String productId;
	private Integer playTime;

	@Builder
	private AppleIAPPurchaseRequest(String transactionId, UUID appAccountToken, Long pencilQuantity, Long price,
		String productId , Integer playTime) {
		this.transactionId = transactionId;
		this.appAccountToken = appAccountToken;
		this.pencilQuantity = pencilQuantity;
		this.price = price;
		this.productId = productId;
		this.playTime = playTime;
	}

	public static AppleIAPPurchaseRequest of(String transactionId, UUID appAccountToken, Long pencilQuantity,
		Long price, String productId, Integer playTime) {
		return AppleIAPPurchaseRequest.builder()
			.transactionId(transactionId)
			.appAccountToken(appAccountToken)
			.pencilQuantity(pencilQuantity)
			.price(price)
			.productId(productId)
			.playTime(playTime)
			.build();
	}

	public static AppleIAPPurchaseRequest ofRetry(PurchasedPencil pencil) {
		return AppleIAPPurchaseRequest.builder()
			.transactionId(pencil.getTransactionId())
			.pencilQuantity(pencil.getPurchaseQuantity())
			.price(pencil.getPrice())
			.playTime(pencil.getPlayTime())
			.appAccountToken(pencil.getAppAccountToken())
			.build();
	}

}
