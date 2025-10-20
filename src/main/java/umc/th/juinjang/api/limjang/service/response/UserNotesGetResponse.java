package umc.th.juinjang.api.limjang.service.response;

import java.util.List;
import java.util.Map;

import umc.th.juinjang.domain.image.model.Image;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.limjang.model.LimjangPurpose;

public record UserNotesGetResponse(
	List<UserNoteResponse> notes
) {
	public record UserNoteResponse(
		long noteId,
		LimjangPurpose purposeType,
		LimjangPropertyType propertyType,
		LimjangPriceType priceType,
		String name,
		List<String> imageUrl,
		boolean isScraped,
		String rate,
		String price,
		String monthlyRent,
		Integer pyong,
		String floor,
		String address,
		String shortAddress
	) {
		static UserNoteResponse of(Limjang limjang, boolean isScraped) {
			return new UserNoteResponse(
				limjang.getLimjangId(),
				limjang.getPurpose(),
				limjang.getPropertyType(),
				limjang.getPriceType(),
				limjang.getNickname(),
				limjang.getImageList().stream().map(Image::getImageUrl).limit(3).toList(),
				isScraped,
				limjang.getReport() == null ? null : limjang.getReport().getTotalRate().toString(),
				limjang.getLimjangPrice().getPrice(limjang.getPriceType(), limjang.getPurpose()),
				limjang.getPriceType() == LimjangPriceType.MONTHLY_RENT ?
					limjang.getLimjangPrice().getMonthlyRent() : null,
				limjang.getPyong(),
				limjang.getFloor(),
				limjang.getAddressEntity() != null ? limjang.getAddressEntity().getRoadAddress() : null,
				limjang.getAddressEntity() != null ? limjang.getAddressEntity().getShortAddress() : null
			);
		}
	}

	public static UserNotesGetResponse of(List<Limjang> limjangs, Map<Long, Boolean> isScraped) {
		return new UserNotesGetResponse(
			limjangs.stream().map(it -> UserNoteResponse.of(it, isScraped.get(it.getLimjangId()))).toList());
	}
}