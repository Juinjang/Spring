package umc.th.juinjang.api.pencil.service;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.pencil.purchased.model.PurchasedPencil;
import umc.th.juinjang.domain.pencil.purchased.repository.PurchasedPencilRepository;

@Component
@RequiredArgsConstructor
public class PurchasedPencilUpdater {

	private final PurchasedPencilRepository purchasedPencilRepository;

	public List<PurchasedPencil> findByMemberAndDeliverySuccessRemainQuantityGreaterThanOrderByCreatedAtAsc(
		Member buyer,
		Long remainQuantity) {
		return purchasedPencilRepository.findByMemberAndDeliverySuccessAndRemainQuantityGreaterThanOrderByCreatedAtAsc(
			buyer,
			remainQuantity);
	}

	public void save(PurchasedPencil successPurchase) {
		purchasedPencilRepository.save(successPurchase);
	}
}
