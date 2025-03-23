package umc.th.juinjang.domain.pencil.purchased.model;

import java.util.UUID;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
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

	// TODO : 추후에 ENUM 으로 변경 필요
	private String deliveryStatus;
}
