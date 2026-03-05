package umc.th.juinjang.api.pencil.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.pencil.acquired.AcquiredPencilRepository;

@Component
@RequiredArgsConstructor
public class AcquiredPencilFinder {

	private final AcquiredPencilRepository acquiredPencilRepository;

	public boolean existsByMemberId(Long memberId) {
		return acquiredPencilRepository.existsByMemberId(memberId);
	}
}
