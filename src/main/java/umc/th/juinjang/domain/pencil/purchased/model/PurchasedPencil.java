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
	@Column(name = "purchased_pencil_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	private String title;
	private Long purchaseQuantity;
	@Comment("구매 후 사용자에게 남은 연필의 개수")
	private Long remainQuantity;
	@Comment("구매 후 사용하고 남은 연필의 개수")
	private Long usedQuantity;
	private Long price;

	@Comment("애플 인앱 결제에서, 프론트에서 전달해주는 트랜잭션 아이디")
	private String transactionId;

	@Comment("애플 인앱 결제에서, 프론트에서 전달해주는 애플 앱 토큰")
	private UUID appAccountToken;

	@Comment("해당 결제한 연필이 정상적으로 고객에게 전달됐는 지 여부")
	@Convert(converter = DeliveryStatusConverter.class)
	private DeliveryStatus deliveryStatus;

	@Comment("트랜잭션이 정상적으로 진행됐는 가 여부")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TransactionStatus transactionStatus;

	private Integer playTime;

	private Long retryCount = 0L;

	private LocalDateTime purchasedAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	@Builder
	public PurchasedPencil(Member member, String title, Long purchaseQuantity, Long usedQuantity,
		Long remainQuantity, TransactionStatus transactionStatus, Integer playTime,
		Long price, String transactionId, UUID appAccountToken, DeliveryStatus deliveryStatus,
		LocalDateTime purchasedAt) {
		this.member = member;
		this.title = title;
		this.purchaseQuantity = purchaseQuantity;
		this.remainQuantity = remainQuantity;
		this.usedQuantity = usedQuantity;
		this.price = price;
		this.playTime = playTime;
		this.transactionId = transactionId;
		this.appAccountToken = appAccountToken;
		this.deliveryStatus = deliveryStatus;
		this.transactionStatus = transactionStatus;
		this.purchasedAt = purchasedAt;
	}

	private static PurchasedPencilBuilder baseBuilder(
		Member member, String title, Long quantity, Long price,
		Integer playTime, String transactionId, UUID token, LocalDateTime purchasedAt
	) {
		return PurchasedPencil.builder()
			.member(member)
			.title(title)
			.purchaseQuantity(quantity)
			.usedQuantity(quantity)
			.price(price)
			.playTime(playTime)
			.transactionId(transactionId)
			.appAccountToken(token)
			.purchasedAt(purchasedAt);
	}

	// ✅ 결제 성공
	public static PurchasedPencil successOf(Member member, String title, Long quantity, Long remainQuantity,
		Long price, Integer playTime, String transactionId,
		UUID token, LocalDateTime purchasedAt) {
		return baseBuilder(member, title, quantity, price, playTime, transactionId, token, purchasedAt)
			.transactionStatus(TransactionStatus.SUCCESS)
			.deliveryStatus(DeliveryStatus.DELIVERY_SUCCESS)
			.remainQuantity(remainQuantity)
			.build();
	}

	// ✅ 서버 에러
	public static PurchasedPencil failedDueToServerError(Member member, String title, Long quantity,
		Long price, Integer playTime, String transactionId, UUID token, LocalDateTime purchasedAt) {
		return baseBuilder(member, title, quantity, price, playTime, transactionId, token, purchasedAt)
			.transactionStatus(TransactionStatus.DB_FAILED)
			.deliveryStatus(DeliveryStatus.SERVER_ERROR)
			.remainQuantity(0L)
			.build();
	}

	// ✅ 검증 실패
	public static PurchasedPencil failedDueToValidation(Member member, String title, Long quantity,
		Long price, Integer playTime, String transactionId, UUID token, LocalDateTime purchasedAt) {
		return baseBuilder(member, title, quantity, price, playTime, transactionId, token, purchasedAt)
			.transactionStatus(TransactionStatus.VALIDATION_FAILED)
			.deliveryStatus(DeliveryStatus.OTHER_REASONS)
			.remainQuantity(0L)
			.build();
	}

	public void decreaseRemainQuantity(long quantity) {
		this.remainQuantity -= quantity;
	}

	public void markAsSuccess() {
		this.transactionStatus = TransactionStatus.SUCCESS;
		this.deliveryStatus = DeliveryStatus.DELIVERY_SUCCESS;
	}

	public void markAsRefund() {
		this.transactionStatus = TransactionStatus.REFUNDED;
	}

	public void updateRetryCount(Long retryCount) {
		this.retryCount = retryCount;
	}
}


