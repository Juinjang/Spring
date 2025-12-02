package umc.th.juinjang.api.limjang.service.response;

import java.time.format.DateTimeFormatter;
import java.util.List;

import umc.th.juinjang.domain.image.model.Image;
import umc.th.juinjang.domain.limjang.model.Address;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.limjang.model.LimjangPurpose;

public record UserNoteGetResponse(
	boolean isShared,
	LimjangPurpose purposeType,
	LimjangPropertyType propertyType,
	LimjangPriceType priceType,
	String buildingName,
	List<String> images,
	String roadAddress,
	String addressDetail,
	String price,
	String monthlyRent,
	String updatedAt,
	String floor,
	Integer pyong,
	String bcode,
	String sido,
	String sigungu,
	String bname1,
	String bname2
) {
	public static UserNoteGetResponse of(boolean isShared, Limjang note, Address address) {
		return new UserNoteGetResponse(
			isShared,
			note.getPurpose(),
			note.getPropertyType(),
			note.getPriceType(),
			note.getNickname(),
			note.getImageList().stream().map(Image::getImageUrl).limit(3).toList(),
			address != null ? address.getRoadAddress() : null,
			address != null ? address.getAddressDetail() : null,
			note.getLimjangPrice().getPrice(note.getPriceType(), note.getPurpose()),
			note.getPriceType() == LimjangPriceType.MONTHLY_RENT ? note.getLimjangPrice().getMonthlyRent() : null,
			note.getUpdatedAt().format(DateTimeFormatter.ofPattern("yy.MM.dd")),
			note.getFloor(),
			note.getPyong(),
			address != null ? address.getBcode() : null,
			address != null ? address.getSido() : null,
			address != null ? address.getSigungo() : null,
			address != null ? address.getBname1() : null,
			address != null ? address.getBname2() : null
		);
	}

	// TODO : 이미지 저장 및 URL 변경 필요
	public static UserNoteGetResponse mock() {
		return new UserNoteGetResponse(
			true,
			LimjangPurpose.RESIDENTIAL_PURPOSE,
			LimjangPropertyType.APARTMENT,
			LimjangPriceType.SALE,
			"우성 아파트",
			List.of("https://example.com/image1.jpg", "https://example.com/image2.jpg",
				"https://example.com/image3.jpg"),
			"서울 송파구 잠실동",
			"101-1",
			"3010000000",
			null,
			"23.12.01",
			"10",
			28,
			"1168010100",
			"서울특별시",
			"송파구",
			"잠실동",
			""
		);
	}
}
