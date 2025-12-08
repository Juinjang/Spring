package umc.th.juinjang.api.limjang.service.response;

import static umc.th.juinjang.api.limjang.service.LimjangPriceBridge.*;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import umc.th.juinjang.domain.checklist.model.LimjangCheckListVersion;
import umc.th.juinjang.domain.image.model.Image;
import umc.th.juinjang.domain.limjang.model.Limjang;

@Builder
public record LimjangDetailGetResponse(
	long limjangId,
	LimjangCheckListVersion checkListVersion,
	List<String> images,
	int purposeCode,
	String nickname,
	int priceType,
	List<String> priceList,
	String address,
	String addressDetail,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static LimjangDetailGetResponse of(Limjang limjang) {
		return LimjangDetailGetResponse.builder()
			.limjangId(limjang.getLimjangId())
			.checkListVersion(LimjangCheckListVersion.getByLimjangType(limjang))
			.images(limjang.getImageList().stream().map(Image::getImageUrl).toList())
			.purposeCode(limjang.getPurpose().getValue())
			.nickname(limjang.getNickname())
			.priceType(limjang.getPriceType().getValue())
			.priceList(makePriceListVersion2(limjang.getPriceType(), limjang.getPurpose(), limjang.getLimjangPrice()))
			.address(limjang.getAddress())
			.addressDetail(limjang.getAddressDetail())
			.createdAt(limjang.getCreatedAt())
			.updatedAt(limjang.getUpdatedAt())
			.build();
	}

	public static LimjangDetailGetResponse mock() {
		return LimjangDetailGetResponse.builder()
			.limjangId(1L)
			.checkListVersion(LimjangCheckListVersion.LIMJANG)
			.images(List.of("https://juinjang-bucket.s3.ap-northeast-2.amazonaws.com/mock/mock-png_1.jpeg",
				"https://juinjang-bucket.s3.ap-northeast-2.amazonaws.com/mock/mock-png_2.jpeg",
				"https://juinjang-bucket.s3.ap-northeast-2.amazonaws.com/mock/mock-png_3.jpeg"))
			.purposeCode(1)
			.nickname("우성 아파트")
			.priceType(0)
			.priceList(List.of("500000000"))
			.address("서울특별시 강남구 테헤란로 123")
			.addressDetail("101동 1001호")
			.createdAt(LocalDateTime.of(2024, 11, 15, 10, 30))
			.updatedAt(LocalDateTime.of(2024, 11, 20, 14, 20))
			.build();
	}
}
