package umc.th.juinjang.api.pencil.service;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredPencil;
import umc.th.juinjang.domain.pencil.acquired.repository.AcquiredPencilRepository;

@Component
@RequiredArgsConstructor
public class AcquiredPencilFinder {

	private final AcquiredPencilRepository acquiredPencilRepository;

	public List<AcquiredPencil> findAllByMemberOrderByCreatedAtDesc(Member member) {
		return acquiredPencilRepository.findAllByMemberOrderByCreatedAtDesc(member);
	}

	public AcquiredPencil findById(Long id) {
		return acquiredPencilRepository.findById(id).orElse(null);
	}

	public boolean existsByMember(Member member) {
		return acquiredPencilRepository.existsByMember(member);
	}
}
