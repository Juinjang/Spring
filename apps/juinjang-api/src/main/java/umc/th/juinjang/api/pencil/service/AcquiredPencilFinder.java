package umc.th.juinjang.api.pencil.service;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.pencil.service.response.AcquiredPencilResponse;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredPencil;
import umc.th.juinjang.domain.pencil.acquired.repository.AcquiredPencilRepository;

@Component
@RequiredArgsConstructor
public class AcquiredPencilFinder {

	private final AcquiredPencilRepository acquiredPencilRepository;

	public List<AcquiredPencilResponse> findAllByMemberOrderByCreatedAtDesc(Member member) {
		return acquiredPencilRepository.findAllByMemberWithBuildingNameOrderByCreatedAtDesc(member);
	}

	public boolean existsByMemberAndIsReadFalse(Member member) {
		return acquiredPencilRepository.existsByMemberAndIsReadFalse(member);
	}

	public AcquiredPencil findById(Long id) {
		return acquiredPencilRepository.findById(id).orElse(null);
	}

	public boolean existsByMember(Member member) {
		return acquiredPencilRepository.existsByMember(member);
	}
}
