package umc.th.juinjang.api.pencil.service;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.pencil.used.model.UsedPencil;
import umc.th.juinjang.domain.pencil.used.repository.UsedPencilRepository;

@Component
@RequiredArgsConstructor
public class UsedPencilFinder {

	private final UsedPencilRepository usedPencilRepository;

	public boolean existsByMemberAndSharedNoteId(Member member, long sharedNoteId) {
		return usedPencilRepository.existsByMemberAndSharedNoteId(member, sharedNoteId);
	}

	public List<UsedPencil> findAllByMemberOrderByCreatedAtDesc(Member member) {
		return usedPencilRepository.findAllByMemberOrderByCreatedAtDesc(member);
	}
}
