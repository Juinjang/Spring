package umc.th.juinjang.event.publisher;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.event.RewardViewCountEvent;

@RequiredArgsConstructor
@Component
public class ApplicationRewardViewCountPublisherAdapter implements RewardViewCountPublisher {

	private final ApplicationEventPublisher applicationEventPublisher;

	@Override
	public void checkViewCountRewardPolicy(Member member, Long sharedNoteId, Long viewCount) {
		applicationEventPublisher.publishEvent(RewardViewCountEvent.of(member, sharedNoteId, viewCount));
	}
}
