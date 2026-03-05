package umc.th.juinjang.api.limjang.service.response;

import static umc.th.juinjang.api.limjang.service.LimjangPriceBridge.makePriceListVersion2;

import java.util.List;
import java.util.Map;
import umc.th.juinjang.domain.image.model.Image;
import umc.th.juinjang.domain.limjang.model.Limjang;

public record LimjangsGetByKeywordResponse(List<LimjangByKeywordResponse> limjangList) {
  record LimjangByKeywordResponse(
      long limjangId,
      List<String> images,
      int purposeCode,
      boolean isScraped,
      String nickname,
      int priceType,
      List<String> priceList,
      String totalAverage,
      String address
  ) {
    static LimjangByKeywordResponse of(Limjang limjang, boolean isScraped) {
      return new LimjangByKeywordResponse(
          limjang.getLimjangId(),
          limjang.getImageList().stream().map(Image::getImageUrl).toList(),
          limjang.getPurpose().getValue(),
          isScraped,
          limjang.getNickname(),
          limjang.getPriceType().getValue(),
          makePriceListVersion2(limjang.getPriceType(), limjang.getPurpose(), limjang.getLimjangPrice()),
          limjang.getReport() == null ? null : limjang.getReport().getTotalRate().toString(),
          limjang.getAddress()
      );
    }
  }

  public static LimjangsGetByKeywordResponse of(List<Limjang> limjangList, Map<Long, Boolean> mapLimjangToScrapStatus) {
    return new LimjangsGetByKeywordResponse(limjangList.stream().map(it -> LimjangByKeywordResponse.of(it, mapLimjangToScrapStatus.get(it.getLimjangId()))).toList());
  }
}
