package umc.th.juinjang.domain.pencilaccount.model;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class PencilAccount extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pencilAccountId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false, unique = true)
	private Member member;

	private Long acquiredBalance;

	private Long purchasedBalance;

	private Long totalBalance;

	private Long totalPurchaseAmount;

	@Comment("환불 시에, 해당 멤버가 현재까지 얼마나 환불했는지에 대한 정보가 필요함.")
	private Long totalRefundAmount;

	@Builder
	private PencilAccount(Member member) {
		this.member = member;
		this.acquiredBalance = 0L;
		this.purchasedBalance = 0L;
		this.totalBalance = 0L;
		this.totalPurchaseAmount = 0L;
		this.totalRefundAmount = 0L;
	}

	public static PencilAccount createPencilAccount(Member member) {
		return PencilAccount.builder()
			.member(member).build();

	}
}
