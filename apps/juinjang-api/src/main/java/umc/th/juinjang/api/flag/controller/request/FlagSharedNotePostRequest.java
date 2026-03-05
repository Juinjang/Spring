package umc.th.juinjang.api.flag.controller.request;

import umc.th.juinjang.domain.flag.model.FlagSharedNoteType;

public record FlagSharedNotePostRequest(
	Long sharedNoteId,
	FlagSharedNoteType type
) {
}
