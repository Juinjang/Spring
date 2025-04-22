package umc.th.juinjang.api.pencil.service.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import umc.th.juinjang.domain.pencil.purchased.model.PurchasedPencil;

@Getter
public class PurchasedPencilsResponse {
	private Long purchasePencilId;
	private Long purchaseQuantity;
	private Long remainQuantity;
	private String title;
	private Long price;
	private LocalDateTime createdAt;

	@Builder
	public PurchasedPencilsResponse(Long purchasePencilId, Long purchaseQuantity, Long remainQuantity,
		String title, Long price, LocalDateTime createdAt) {
		this.purchasePencilId = purchasePencilId;
		this.purchaseQuantity = purchaseQuantity;
		this.remainQuantity = remainQuantity;
		this.title = title;
		this.price = price;
		this.createdAt = createdAt;
	}

	public static PurchasedPencilsResponse from(PurchasedPencil purchasedPencil) {
		return PurchasedPencilsResponse.builder()
			.purchasePencilId(purchasedPencil.getPurchasedPencilId())
			.purchaseQuantity(purchasedPencil.getPurchaseQuantity())
			.remainQuantity(purchasedPencil.getRemainQuantity())
			.title(purchasedPencil.getTitle())
			.price(purchasedPencil.getPrice())
			.createdAt(purchasedPencil.getCreatedAt())
			.build();
	}
}
