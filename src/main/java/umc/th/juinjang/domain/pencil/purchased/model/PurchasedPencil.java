package umc.th.juinjang.domain.pencil.purchased.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.th.juinjang.domain.member.model.Member;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Entity
public class PurchasedPencil {

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

	@Comment("애플 인앱 결제에서, 프론트에서 전달해주는 애플 앱 토큰")
	private UUID appAccountToken;

	@Convert(converter = DeliveryStatusConverter.class)
	private DeliveryStatus deliveryStatus;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TransactionStatus transactionStatus;

	private Long playTime;

	private Long retryCount = 0L;

	private LocalDateTime purchasedAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	@Builder
	public PurchasedPencil(Member member, String title, Long purchaseQuantity,
		Long remainQuantity, TransactionStatus transactionStatus, Long playTime,
		Long price, String transactionId, UUID appAccountToken, DeliveryStatus deliveryStatus,
		LocalDateTime purchasedAt) {
		this.member = member;
		this.title = title;
		this.purchaseQuantity = purchaseQuantity;
		this.remainQuantity = remainQuantity;
		this.price = price;
		this.playTime = playTime;
		this.transactionId = transactionId;
		this.appAccountToken = appAccountToken;
		this.deliveryStatus = deliveryStatus;
		this.transactionStatus = transactionStatus;
		this.purchasedAt = purchasedAt;
	}

	public void decreaseRemainQuantity(long quantity) {
		this.remainQuantity -= quantity;
	}

	public void markAsSuccess(){
		this.transactionStatus = TransactionStatus.SUCCESS;
		this.deliveryStatus = DeliveryStatus.DELIVERY_SUCCESS;
	}

	public void updateRetryCount(Long retryCount) {
		this.retryCount = retryCount;
	}

	private static PurchasedPencilBuilder baseBuilder(
		Member member, String title, Long quantity, Long price,
		Long playTime, String transactionId, UUID token, LocalDateTime purchasedAt
	) {
		return PurchasedPencil.builder()
			.member(member)
			.title(title)
			.purchaseQuantity(quantity)
			.remainQuantity(quantity)
			.price(price)
			.playTime(playTime)
			.transactionId(transactionId)
			.appAccountToken(token)
			.purchasedAt(purchasedAt);
	}

	// ✅ 결제 성공
	public static PurchasedPencil successOf(Member member, String title, Long quantity,
		Long price, Long playTime, String transactionId,
		UUID token, LocalDateTime purchasedAt) {
		return baseBuilder(member, title, quantity, price, playTime, transactionId, token, purchasedAt)
			.transactionStatus(TransactionStatus.SUCCESS)
			.deliveryStatus(DeliveryStatus.DELIVERY_SUCCESS)
			.build();
	}

	// ✅ 서버 에러
	public static PurchasedPencil failedDueToServerError(Member member, String title, Long quantity,
		Long price, Long playTime, String transactionId, UUID token, LocalDateTime purchasedAt) {
		return baseBuilder(member, title, quantity, price, playTime, transactionId, token, purchasedAt)
			.transactionStatus(TransactionStatus.DB_FAILED)
			.deliveryStatus(DeliveryStatus.SERVER_ERROR)
			.build();
	}

	// ✅ 검증 실패
	public static PurchasedPencil failedDueToValidation(Member member, String title, Long quantity,
		Long price, Long playTime, String transactionId, UUID token, LocalDateTime purchasedAt) {
		return baseBuilder(member, title, quantity, price, playTime, transactionId, token, purchasedAt)
			.transactionStatus(TransactionStatus.VALIDATION_FAILED)
			.deliveryStatus(DeliveryStatus.OTHER_REASONS)
			.build();
	}
}


