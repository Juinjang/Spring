package umc.th.juinjang.api.limjang.service.converter;

import static umc.th.juinjang.api.limjang.service.LimjangPriceBridge.getPriceToString;

import java.util.Optional;
import umc.th.juinjang.api.limjang.service.response.LimjangsMainGetResponse.LimjangMainResponse;
import umc.th.juinjang.domain.image.model.Image;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.report.model.Report;

public class LimjangsMainGetResponseConverter {

  public static LimjangMainResponse toLimjangMainResponse(final Limjang limjang) {
    return new LimjangMainResponse(
        limjang.getLimjangId(),
        limjang.getImageList().stream().findFirst().map(Image::getImageUrl).orElse(null),
        limjang.getNickname(), getPriceToString(limjang),
        getTotalAverageOrElse(limjang),
        limjang.getAddress());
  }

  private static String getTotalAverageOrElse(final Limjang limjang) {
    return Optional.ofNullable(limjang.getReport()).map(Report::getTotalRate).map(Object::toString).orElse(null);
  }
}