package umc.th.juinjang.api.pencil.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.pencil.used.repository.UsedPencilRepository;

@Component
@RequiredArgsConstructor
public class UsedPencilFinder {

	private final UsedPencilRepository usedPencilRepository;

	public boolean existsByMemberAndSharedNoteId(Member member, long sharedNoteId) {
		return usedPencilRepository.existsByMemberAndSharedNoteId(member, sharedNoteId);
	}

	public int countBySharedNoteId(long sharedNoteId) {
		return usedPencilRepository.countBySharedNoteId(sharedNoteId);
	}

}
