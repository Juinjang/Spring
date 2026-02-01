package umc.th.juinjang.api.flag.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.api.flag.controller.request.FlagSharedNotePostRequest;
import umc.th.juinjang.api.note.shared.service.SharedNoteFinder;
import umc.th.juinjang.domain.flag.model.FlagSharedNote;
import umc.th.juinjang.domain.flag.model.FlagSharedNoteType;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.note.shared.model.SharedNote;
import umc.th.juinjang.event.publisher.FlagSharedNoteEventPublisher;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlagSharedNoteCommandService {

	private final SharedNoteFinder sharedNoteFinder;
	private final FlagSharedNoteUpdater flagSharedNoteUpdater;
	private final FlagSharedNoteEventPublisher flagSharedNoteEventPublisher;
	
	@Transactional
	public void createSharedNoteFlag(Member flaggedBy, FlagSharedNotePostRequest flagSharedNotePostRequest) {
		SharedNote flaggedsharedNote = sharedNoteFinder.getByIdWhereDeletedAtIsNull(
			flagSharedNotePostRequest.sharedNoteId());

		flagSharedNoteUpdater.save(
			createFlagSharedNote(flaggedBy, flagSharedNotePostRequest.type(), flaggedsharedNote));
		flagSharedNoteEventPublisher.publishFlagSharedNoteEvent(flaggedBy.getMemberId(),
			flaggedsharedNote.getSharedNoteId(),
			flaggedsharedNote.getMember().getMemberId(),
			flagSharedNotePostRequest.type());
	}

	private FlagSharedNote createFlagSharedNote(Member flaggedBy, FlagSharedNoteType type,
		SharedNote flaggedsharedNote) {
		return FlagSharedNote.create(type, flaggedBy.getMemberId(),
			flaggedsharedNote.getSharedNoteId());
	}
}
