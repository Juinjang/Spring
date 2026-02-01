package umc.th.juinjang.event.publisher;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.flag.model.FlagSharedNoteType;
import umc.th.juinjang.event.FlagSharedNoteEvent;

@RequiredArgsConstructor
@Component
public class ApplicationFlagSharedNoteEventPublisher implements FlagSharedNoteEventPublisher {

	private final ApplicationEventPublisher applicationEventPublisher;

	@Override
	public void publishFlagSharedNoteEvent(Long flaggedByMemberId,
		Long targetSharedNoteId,
		Long targetMemberId,
		FlagSharedNoteType flagSharedNoteType) {
		applicationEventPublisher.publishEvent(FlagSharedNoteEvent.of(flaggedByMemberId, targetSharedNoteId,
			targetMemberId, flagSharedNoteType));
	}
}
