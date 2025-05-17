package umc.th.juinjang.api.pencil.service.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import umc.th.juinjang.domain.pencil.purchased.model.PurchasedPencil;

@Getter
public class PurchasedPencilResponse {
	private Long purchasePencilId;
	private Long purchaseQuantity;
	private Long remainQuantity;
	private String title;
	private Long price;
	private LocalDateTime purchasedAt;

	@Builder
	public PurchasedPencilResponse(Long purchasePencilId, Long purchaseQuantity, Long remainQuantity,
		String title, Long price, LocalDateTime purchasedAt) {
		this.purchasePencilId = purchasePencilId;
		this.purchaseQuantity = purchaseQuantity;
		this.remainQuantity = remainQuantity;
		this.title = title;
		this.price = price;
		this.purchasedAt = purchasedAt;
	}

	public static PurchasedPencilResponse from(PurchasedPencil purchasedPencil) {
		return PurchasedPencilResponse.builder()
			.purchasePencilId(purchasedPencil.getPurchasedPencilId())
			.purchaseQuantity(purchasedPencil.getPurchaseQuantity())
			.remainQuantity(purchasedPencil.getRemainQuantity())
			.title(purchasedPencil.getTitle())
			.price(purchasedPencil.getPrice())
			.purchasedAt(purchasedPencil.getPurchasedAt())
			.build();
	}
}
