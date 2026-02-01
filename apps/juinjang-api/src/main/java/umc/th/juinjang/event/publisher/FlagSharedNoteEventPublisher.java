package umc.th.juinjang.event.publisher;

import umc.th.juinjang.domain.flag.model.FlagSharedNoteType;

public interface FlagSharedNoteEventPublisher {
	void publishFlagSharedNoteEvent(
		Long flaggedByMemberId,
		Long targetSharedNoteId,
		Long targetMemberId,
		FlagSharedNoteType flagSharedNoteType);
}
