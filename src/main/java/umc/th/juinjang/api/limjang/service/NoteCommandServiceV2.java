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

	// public static LimjangPrice determineLimjangPrice(List<String> priceList, Integer purpose, Integer priceType){
	// 	checkExpectedSize(priceType, priceList.size());
	// 	if (purpose == 0){ // 부동산 투자 목적 -> 실거래가
	// 		return LimjangPrice.builder().marketPrice(priceList.get(0)).build();
	// 	} else if (purpose == 1){ // 직접 거래 목적
	// 		switch (priceType){
	// 			case 0 : // 매매
	// 				return LimjangPrice.builder().sellingPrice(priceList.get(0)).build();
	// 			case 1 :// 전세
	// 				return LimjangPrice.builder().pullRent(priceList.get(0)).build();
	// 			case 2 : // 월세 : 0, 보증금 : 1 이 경우 배열 길이는 무조건 2여야만 함.
	// 				return LimjangPrice.builder().depositPrice(priceList.get(0))
	// 					.monthlyRent(priceList.get(1)).build();
	// 			case 3 :
	// 				return LimjangPrice.builder().marketPrice(priceList.get(0)).build();
	// 		}
	// 	}
	// 	return null;
	// }
}
