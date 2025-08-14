package umc.th.juinjang.domain.reward.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.th.juinjang.domain.member.model.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Reward {

	@Id
	@Column(name = "reward_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long rewardId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Enumerated(EnumType.STRING)
	private RewardType type;

	private Long milestone;

	private Long sharedNoteId;

	private Long rewardPencil;

	@Builder
	private Reward(Member member, RewardType type, Long milestone, Long sharedNoteId, Long rewardPencil) {
		this.member = member;
		this.type = type;
		this.milestone = milestone;
		this.sharedNoteId = sharedNoteId;
		this.rewardPencil = rewardPencil;
	}

	public static Reward create(Member member, RewardType type, Long milestone, Long sharedNoteId, Long rewardPencil) {
		return Reward.builder()
			.member(member)
			.type(type)
			.milestone(milestone)
			.sharedNoteId(sharedNoteId)
			.rewardPencil(rewardPencil)
			.build();
	}

}
