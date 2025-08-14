package umc.th.juinjang.api.limjang.service.response;

import static umc.th.juinjang.api.limjang.service.LimjangPriceBridge.makePriceListVersion2;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import umc.th.juinjang.domain.image.model.Image;
import umc.th.juinjang.domain.checklist.model.LimjangCheckListVersion;
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
  public static LimjangDetailGetResponse of(Limjang limjang){
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
}
