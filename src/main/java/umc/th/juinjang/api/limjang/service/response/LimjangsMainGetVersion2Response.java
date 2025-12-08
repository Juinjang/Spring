package umc.th.juinjang.api.limjang.service.response;

import static umc.th.juinjang.api.limjang.service.LimjangPriceBridge.*;

import java.util.List;

import lombok.Builder;
import umc.th.juinjang.domain.limjang.model.Limjang;

public record LimjangsMainGetVersion2Response(List<LimjangMainVersion2Response> recentUpdatedList) {
	@Builder
	record LimjangMainVersion2Response(
		long limjangId,
		int priceType,
		String image,
		String nickname,
		String price,
		String totalAverage,
		String address) {

		static LimjangMainVersion2Response of(Limjang limjang) {
			return LimjangMainVersion2Response.builder()
				.limjangId(limjang.getLimjangId())
				.priceType(limjang.getPriceType().getValue())
				.image(limjang.getDefaultImage())
				.nickname(limjang.getNickname())
				.price(getPriceToString(limjang))
				.totalAverage(limjang.getReport() == null ? null : limjang.getReport().getTotalRate().toString())
				.address(limjang.getAddress())
				.build();
		}
	}

	public static LimjangsMainGetVersion2Response of(List<Limjang> limjangList) {
		return new LimjangsMainGetVersion2Response(limjangList.stream().map(LimjangMainVersion2Response::of).toList());
	}

	public static LimjangsMainGetVersion2Response mock() {
		LimjangMainVersion2Response mockNote = LimjangMainVersion2Response.builder()
			.limjangId(1L)
			.priceType(0)
			.image("https://juinjang-bucket.s3.ap-northeast-2.amazonaws.com/mock/mock-png_1.jpeg")
			.nickname("우성 아파트")
			.price("3010000000")
			.totalAverage("4.5")
			.address("")
			.build();

		return new LimjangsMainGetVersion2Response(List.of(mockNote));
	}
}
