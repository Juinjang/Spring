package umc.th.juinjang.api.pencil.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.pencil.service.response.AcquiredPencilResponse;
import umc.th.juinjang.api.pencil.service.response.PurchasedPencilsResponse;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredPencil;
import umc.th.juinjang.domain.pencil.purchased.model.PurchasedPencil;

@Service
@RequiredArgsConstructor
public class PencilService {

	private final AcquiredPencilFinder acquiredPencilFinder;
	private final PurchasedPencilFinder purchasedPencilFinder;

	public List<AcquiredPencilResponse> getAcquiredPencils(Member member) {
		List<AcquiredPencil> acquiredPencils = acquiredPencilFinder.findAllByMemberOrderByCreatedAtDesc(member);
		return acquiredPencils.stream()
			.map(AcquiredPencilResponse::from)
			.toList();
	}

	public List<PurchasedPencilsResponse> getPurchasedPencils(Member member) {
		List<PurchasedPencil> purchasedPencils = purchasedPencilFinder.findAllByMemberWhereDeliverySuccessOrderByCreatedAtDesc(
			member);
		return purchasedPencils.stream()
			.map(PurchasedPencilsResponse::from)
			.toList();
	}

}
