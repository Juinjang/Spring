package umc.th.juinjang.api.note.liked.service;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.LikedNoteHandler;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.note.liked.model.LikedNote;
import umc.th.juinjang.domain.note.liked.model.repository.LikedNoteRepository;
import umc.th.juinjang.domain.note.shared.model.SharedNote;

@Component
@RequiredArgsConstructor
public class LikedNoteFinder {

	private final LikedNoteRepository likedNoteRepository;

	public boolean existsByMemberAndSharedNote(Member member, SharedNote sharedNote) {
		return likedNoteRepository.existsByMemberAndSharedNote(member, sharedNote);
	}

	public LikedNote getByMemberAndSharedNote(Member member, SharedNote sharedNote) {
		return likedNoteRepository.findByMemberAndSharedNote(member, sharedNote)
			.orElseThrow(() -> new LikedNoteHandler(ErrorStatus.LIKEDNOTE_NOT_FOUND));
	}

	public List<Long> findLikedSharedNoteIds(Member member, List<SharedNote> sharedNotes) {
		return likedNoteRepository.findLikedSharedNoteIds(member, sharedNotes);
	}
}
