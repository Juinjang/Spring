package umc.th.juinjang.api.note.liked.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.note.liked.service.response.LikedNoteDeleteResponse;
import umc.th.juinjang.api.note.liked.service.response.LikedNotePostResponse;
import umc.th.juinjang.api.note.shared.service.SharedNoteFinder;
import umc.th.juinjang.api.note.shared.service.SharedNoteUpdater;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.note.liked.model.LikedNote;
import umc.th.juinjang.domain.note.shared.model.SharedNote;

@Service
@RequiredArgsConstructor
public class LikedNoteCommandService {

	private final LikedNoteUpdater likedNoteUpdater;
	private final SharedNoteFinder sharedNoteFinder;
	private final SharedNoteUpdater sharedNoteUpdater;
	private final LikedNoteFinder likedNoteFinder;
	private final LikedNoteDeleter likedNoteDeleter;

	@Transactional
	public LikedNotePostResponse createLikedNote(Member member, Long sharedNoteId) {
		SharedNote sharedNote = sharedNoteFinder.getById(sharedNoteId);
		LikedNote likedNote = LikedNote.create(member, sharedNote);

		likedNoteUpdater.save(likedNote);
		sharedNoteUpdater.incrementLikedCountById(sharedNoteId);

		return new LikedNotePostResponse(sharedNoteFinder.getLikedNoteById(sharedNoteId));
	}

	@Transactional
	public LikedNoteDeleteResponse deleteLikedNote(Member member, Long sharedNoteId) {
		SharedNote sharedNote = sharedNoteFinder.getById(sharedNoteId);
		LikedNote likedNote = likedNoteFinder.getByMemberAndSharedNote(member, sharedNote);

		likedNoteDeleter.delete(likedNote);
		sharedNoteUpdater.decrementLikedCountById(sharedNoteId);

		return new LikedNoteDeleteResponse(sharedNoteFinder.getLikedNoteById(sharedNoteId));
	}

}
