package umc.th.juinjang.api.pencil.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.pencil.service.response.AcquiredPencilResponse;
import umc.th.juinjang.api.pencil.service.response.PurchasedPencilResponse;
import umc.th.juinjang.api.pencil.service.response.UsedPencilResponse;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredPencil;
import umc.th.juinjang.domain.pencil.purchased.model.PurchasedPencil;
import umc.th.juinjang.domain.pencil.used.model.UsedPencil;

@Service
@RequiredArgsConstructor
public class PencilQueryService {

	private final AcquiredPencilFinder acquiredPencilFinder;
	private final PurchasedPencilFinder purchasedPencilFinder;
	private final UsedPencilFinder usedPencilFinder;

	public List<AcquiredPencilResponse> getAcquiredPencils(Member member) {
		List<AcquiredPencil> acquiredPencils = acquiredPencilFinder.findAllByMemberOrderByCreatedAtDesc(member);
		return acquiredPencils.stream()
			.map(AcquiredPencilResponse::from)
			.toList();
	}

	public List<PurchasedPencilResponse> getPurchasedPencils(Member member) {
		List<PurchasedPencil> purchasedPencils = purchasedPencilFinder.findAllByMemberWhereDeliverySuccessOrderByCreatedAtDesc(
			member);
		return purchasedPencils.stream()
			.map(PurchasedPencilResponse::from)
			.toList();
	}

	public List<UsedPencilResponse> getUsedPencils(Member member) {
		List<UsedPencil> usedPencils = usedPencilFinder.findAllByMemberOrderByCreatedAtDesc(member);
		return usedPencils.stream()
			.map(UsedPencilResponse::from)
			.toList();
	}

	public boolean isAcquiredPencilReadStatus(Member member) {
		// false 인 것이 존재하면 안됨.
		return !acquiredPencilFinder.existsByMemberAndIsReadFalse(member);
	}
}
