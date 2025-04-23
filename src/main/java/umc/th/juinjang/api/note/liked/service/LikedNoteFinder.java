package umc.th.juinjang.api.note.liked.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.note.liked.model.repository.LikedNoteRepository;
import umc.th.juinjang.domain.note.shared.model.SharedNote;

@Component
@RequiredArgsConstructor
public class LikedNoteFinder {

	private final LikedNoteRepository likedNoteRepository;

	public boolean existsByMemberAndSharedNote(Member member, SharedNote sharedNote) {
		return likedNoteRepository.existsByMemberAndSharedNote(member, sharedNote);
	}
}
