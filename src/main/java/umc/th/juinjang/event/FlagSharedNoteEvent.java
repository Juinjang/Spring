package umc.th.juinjang.event;

import umc.th.juinjang.domain.flag.model.FlagSharedNoteType;

public record FlagSharedNoteEvent(
	Long flaggedByMemberId,
	Long targetSharedNoteId,
	Long targetMemberId,
	FlagSharedNoteType flagSharedNoteType
) {
	public static FlagSharedNoteEvent of(Long flaggedByMemberId,
		Long targetSharedNoteId,
		Long targetMemberId,
		FlagSharedNoteType flagSharedNoteType) {
		return new FlagSharedNoteEvent(flaggedByMemberId, targetSharedNoteId, targetMemberId, flagSharedNoteType);
	}
}
