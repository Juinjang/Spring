package umc.th.juinjang.api.pencil.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.pencil.service.response.AcquiredPencilResponse;
import umc.th.juinjang.api.pencilAccount.service.PencilAccountService;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredPencil;

@Service
@RequiredArgsConstructor
public class AcquiredPencilService {

	private final AcquiredPencilFinder acquiredPencilFinder;
	private final AcquiredPencilUpdater acquiredPencilUpdater;
	private final PencilAccountService pencilAccountService;

	public List<AcquiredPencilResponse> getAcquiredPencils(Member member) {
		List<AcquiredPencil> acquiredPencils = acquiredPencilFinder.findAllByMember(member);
		return acquiredPencils.stream()
			.map(AcquiredPencilResponse::from)
			.toList();
	}

}
