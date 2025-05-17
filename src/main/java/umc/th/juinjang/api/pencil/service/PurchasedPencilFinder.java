package umc.th.juinjang.api.pencil.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.pencil.purchased.model.PurchasedPencil;
import umc.th.juinjang.domain.pencil.purchased.repository.PurchasedPencilRepository;

@Component
@RequiredArgsConstructor
public class PurchasedPencilFinder {
	private final PurchasedPencilRepository purchasedPencilRepository;

	public List<PurchasedPencil> findAllByMemberWhereDeliverySuccessOrderByCreatedAtDesc(Member member) {
		return purchasedPencilRepository.findAllByMemberWhereDeliverySuccessOrderByCreatedAtDesc(member);
	}

	public Optional<PurchasedPencil> findByTransactionIdAndMember(String transactionId, Member member) {
		return purchasedPencilRepository.findByTransactionIdAndMember(transactionId, member);
	}
}
