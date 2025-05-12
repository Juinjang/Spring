package umc.th.juinjang.event;

import umc.th.juinjang.domain.member.model.Member;

public record RewardViewCountEvent(
	Member member,
	Long sharedNoteId,
	Long viewCount
) {
	public static RewardViewCountEvent of(Member member, Long sharedNoteId, Long viewCount) {
		return new RewardViewCountEvent(member, sharedNoteId, viewCount);
	}
}
