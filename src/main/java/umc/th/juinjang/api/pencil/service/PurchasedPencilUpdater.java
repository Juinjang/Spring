package umc.th.juinjang.api.pencil.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.pencil.purchased.model.PurchasedPencil;
import umc.th.juinjang.domain.pencil.purchased.repository.PurchasedPencilRepository;

@Component
@RequiredArgsConstructor
public class PurchasedPencilUpdater {

	private final PurchasedPencilRepository purchasedPencilRepository;

	public void save(PurchasedPencil purchasedPencil) {
		purchasedPencilRepository.save(purchasedPencil);
	}
}
