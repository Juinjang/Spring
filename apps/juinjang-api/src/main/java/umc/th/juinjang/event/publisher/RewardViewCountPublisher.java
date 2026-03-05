package umc.th.juinjang.event.publisher;

import umc.th.juinjang.domain.member.model.Member;

public interface RewardViewCountPublisher {
	void checkViewCountRewardPolicy(Member member, Long sharedNoteId, Long viewCount);
}
