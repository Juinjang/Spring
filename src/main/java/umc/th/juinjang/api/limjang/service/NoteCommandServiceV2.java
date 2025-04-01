package umc.th.juinjang.api.limjang.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.limjang.controller.request.NotePostRequest;
import umc.th.juinjang.api.address.service.AddressUpdater;
import umc.th.juinjang.domain.limjang.model.Address;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.model.LimjangPrice;
import umc.th.juinjang.domain.limjang.repository.LimjangPriceFactory;
import umc.th.juinjang.domain.member.model.Member;

@Service
@RequiredArgsConstructor
public class NoteCommandServiceV2 {

	private final AddressUpdater addressUpdater;
	private final NoteUpdater noteUpdater;
	private final NotePriceUpdater notePriceUpdater;

	public void createNote(NotePostRequest request, Member member) {
		LimjangPrice limjangPrice = createLimjangPrice(request);
		notePriceUpdater.save(limjangPrice);

		Address address = createAddress(request);
		addressUpdater.save(address);

		int rewardPencil = 3; // 추후 바뀜
		Limjang limjang = Limjang.create(member, limjangPrice, request.purposeType(), request.propertyType(),
			request.priceType(), rewardPencil, request.nickname(), address);

		noteUpdater.save(limjang);
	}

	private LimjangPrice createLimjangPrice(NotePostRequest request) {
		return LimjangPriceFactory.create(request.purposeType(), request.priceType(),
			request.price(), request.monthlyRent());
	}

	private Address createAddress(NotePostRequest request) {
		return Address.create(request.roadAddress(), request.addressDetail(), request.bcode(),
			request.sido(), request.sigungu(),
			request.bname1(), request.bname2());
	}
}
