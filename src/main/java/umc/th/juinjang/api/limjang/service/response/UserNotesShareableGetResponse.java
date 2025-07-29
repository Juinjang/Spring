package umc.th.juinjang.api.limjang.service.response;

import java.util.List;
import java.util.Map;

import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.limjang.model.LimjangPurpose;

public record UserNotesShareableGetResponse(
	List<UserNoteShareableResponse> notes
) {
	record UserNoteShareableResponse(
		long noteId,
		LimjangPurpose purposeType,
		LimjangPropertyType propertyType,
		LimjangPriceType priceType,
		String name,
		String imageUrl,
		boolean isScraped,
		String rate,
		String price,
		String monthlyRent,
		Integer pyong,
		String floor,
		String shortAddress,
		Long rewardPencil
	) {

		static UserNoteShareableResponse of(Limjang limjang, String imageUrl, boolean isScraped, Long rewardPencil) {
			return new UserNoteShareableResponse(
				limjang.getLimjangId(), limjang.getPurpose(), limjang.getPropertyType(), limjang.getPriceType(),
				limjang.getNickname(),
				imageUrl,
				isScraped,
				limjang.getReport() == null ? null : limjang.getReport().getTotalRate().toString(),
				limjang.getLimjangPrice().getPrice(limjang.getPriceType(), limjang.getPurpose()),
				limjang.getPriceType() == LimjangPriceType.MONTHLY_RENT ? limjang.getLimjangPrice().getMonthlyRent() :
					null,
				limjang.getPyong(),
				limjang.getFloor(),
				limjang.getAddressEntity().getShortAddress(),
				rewardPencil
			);
		}
	}

	public static UserNotesShareableGetResponse of(List<Limjang> limjangs, Map<Long, String> imageUrl,
		Map<Long, Boolean> isScraped, Map<Long, Long> expectedReward) {
		return new UserNotesShareableGetResponse(
			limjangs.stream()
				.map(it -> UserNoteShareableResponse.of(it, imageUrl.get(it.getLimjangId()),
					isScraped.get(it.getLimjangId()), expectedReward.get(it.getLimjangId())))
				.toList());
	}
}

