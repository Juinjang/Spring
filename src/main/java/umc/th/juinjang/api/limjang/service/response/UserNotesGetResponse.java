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

	public static UserNotesGetResponse mock() {
		UserNoteResponse mockNote1 = new UserNoteResponse(
			1L,
			LimjangPurpose.RESIDENTIAL_PURPOSE,
			LimjangPropertyType.APARTMENT,
			LimjangPriceType.SALE,
			"우성 아파트",
			List.of("https://juinjang-bucket.s3.ap-northeast-2.amazonaws.com/mock/mock_png_1.png",
				"https://juinjang-bucket.s3.ap-northeast-2.amazonaws.com/mock/mock_png_2.png",
				"https://juinjang-bucket.s3.ap-northeast-2.amazonaws.com/mock/mock_png_3.png"),
			true,
			"4.6",
			"3010000000",
			null,
			28,
			"10",
			"서울 송파구 잠실동 101-1",
			"서울시"
		);

		return new UserNotesGetResponse(List.of(mockNote1));
	}
}