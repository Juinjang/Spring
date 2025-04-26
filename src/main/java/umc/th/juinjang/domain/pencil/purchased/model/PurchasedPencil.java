package umc.th.juinjang.domain.pencil.purchased.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.th.juinjang.domain.common.BaseEntity;
import umc.th.juinjang.domain.member.model.Member;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PurchasedPencil extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long purchasedPencilId;

	@ManyToOne
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	private String title;

	private Long purchaseQuantity;

	private Long remainQuantity;

	private Long price;

	@Comment("애플 인앱 결제에서, 프론트에서 전달해주는 트랜잭션 아이디")
	private String transactionId;

	private UUID appAccountToken;

	@Convert(converter = DeliveryStatusConverter.class)
	private DeliveryStatus deliveryStatus;

	@Builder
	public PurchasedPencil(Member member, String title, Long purchaseQuantity,
		Long remainQuantity,
		Long price, String transactionId, UUID appAccountToken, DeliveryStatus deliveryStatus,
		LocalDateTime createdAt) {
		this.member = member;
		this.title = title;
		this.purchaseQuantity = purchaseQuantity;
		this.remainQuantity = remainQuantity;
		this.price = price;
		this.transactionId = transactionId;
		this.appAccountToken = appAccountToken;
		this.deliveryStatus = deliveryStatus;
		setCreatedAt(createdAt);
	}

	public static PurchasedPencil createSuccessPurchase(Member member, String title,
		Long purchaseQuantity, Long price,
		String transactionId, UUID appAccountToken, LocalDateTime createdAt) {
		return PurchasedPencil.builder()
			.member(member)
			.title(title)
			.purchaseQuantity(purchaseQuantity)
			.remainQuantity(purchaseQuantity)
			.price(price)
			.transactionId(transactionId)
			.appAccountToken(appAccountToken)
			.deliveryStatus(DeliveryStatus.DELIVERY_SUCCESS)
			.createdAt(createdAt)
			.build();
	}

	public static PurchasedPencil createServerErrorPurchase(Member member, String title,
		Long purchaseQuantity, Long price,
		String transactionId, UUID appAccountToken, LocalDateTime createdAt) {
		return PurchasedPencil.builder()
			.member(member)
			.title(title)
			.purchaseQuantity(purchaseQuantity)
			.remainQuantity(purchaseQuantity)
			.price(price)
			.transactionId(transactionId)
			.appAccountToken(appAccountToken)
			.deliveryStatus(DeliveryStatus.SERVER_ERROR)
			.createdAt(createdAt)
			.build();
	}
}

