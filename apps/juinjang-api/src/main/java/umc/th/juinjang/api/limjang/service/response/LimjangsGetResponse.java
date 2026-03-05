package umc.th.juinjang.api.limjang.service.response;

import static umc.th.juinjang.api.limjang.service.LimjangPriceBridge.makePriceListVersion2;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import umc.th.juinjang.domain.image.model.Image;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.model.LimjangPrice;

public record LimjangsGetResponse(List<LimjangsResponse> limjangList) {
  @Builder
   record LimjangsResponse(
      long limjangId,
      List<String> images,
      int purposeCode,
      boolean isScraped,
      String nickname,
      int priceType,
      List<String> priceList,
      String totalAverage,
      String address) {
    static LimjangsResponse of(Limjang limjang, LimjangPrice limjangPrice, boolean isScraped) {
      return LimjangsResponse.builder()
          .limjangId(limjang.getLimjangId())
          .images(getUrlListByScrap(limjang, isScraped))
          .nickname(limjang.getNickname())
          .isScraped(isScraped)
          .purposeCode(limjang.getPurpose().getValue())
          .priceType(limjang.getPriceType().getValue())
          .priceList(makePriceListVersion2(limjang.getPriceType(), limjang.getPurpose(), limjangPrice))
          .totalAverage(limjang.getReport() == null ? null : limjang.getReport().getTotalRate().toString())
          .address(limjang.getAddress())
          .build();
    }
  }

  public static LimjangsGetResponse of(List<Limjang> limjangList, Map<Long, Boolean> mapLimjangToScrapStatus) {
    return new LimjangsGetResponse(limjangList.stream().map(limjang -> LimjangsResponse.of(limjang, limjang.getLimjangPrice(), mapLimjangToScrapStatus.get(limjang.getLimjangId()))).toList());
  }

  private static List<String> getUrlListByScrap(Limjang limjang, boolean isScraped) {
    return limjang.getImageList().stream().map(Image::getImageUrl).limit(!isScraped ? 1 : 3).toList();
  }
}
