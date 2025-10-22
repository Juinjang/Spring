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
}
