package umc.th.juinjang.api.limjang.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.limjang.model.LimjangPrice;
import umc.th.juinjang.domain.limjang.repository.LimjangPriceRepository;

@Component
@RequiredArgsConstructor
public class NotePriceUpdater {
	private final LimjangPriceRepository limjangPriceRepository;

	protected void save(LimjangPrice limjangPrice) {
		limjangPriceRepository.save(limjangPrice);
	}
}
