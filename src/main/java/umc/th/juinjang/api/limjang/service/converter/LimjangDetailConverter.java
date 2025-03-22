package umc.th.juinjang.api.limjang.service.converter;

import static umc.th.juinjang.api.limjang.service.LimjangPriceBridge.makePriceListVersion2;

import java.util.Comparator;
import java.util.List;
import umc.th.juinjang.api.limjang.service.response.LimjangDetailResponseDTO;
import umc.th.juinjang.domain.checklist.model.LimjangCheckListVersion;
import umc.th.juinjang.domain.image.model.Image;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.model.LimjangPrice;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.limjang.model.LimjangPurpose;

public class LimjangDetailConverter {

  public static LimjangDetailResponseDTO.DetailDto toDetail(
      Limjang limjang, LimjangPrice limjangPrice) {

    List<String> urlList = limjang.getImageList().stream()
        .sorted(Comparator.comparing(Image::getCreatedAt)) // createdAt 기준으로 정렬
        .map(Image::getImageUrl)
        .limit(3)
        .toList();

    LimjangPurpose purposeType = limjang.getPurpose();
    LimjangPropertyType propertyType = limjang.getPropertyType();
    LimjangPriceType priceType = limjang.getPriceType();

    LimjangCheckListVersion checkListVersion;
    if (purposeType == LimjangPurpose.RESIDENTIAL_PURPOSE &&
        (propertyType == LimjangPropertyType.VILLA||
            propertyType == LimjangPropertyType.OFFICE_TEL)
    ){
      checkListVersion = LimjangCheckListVersion.NON_LIMJANG;
    } else {
      checkListVersion = LimjangCheckListVersion.LIMJANG;
    }

    List<String> priceList = makePriceListVersion2(priceType, purposeType,limjangPrice);

    return LimjangDetailResponseDTO.DetailDto.builder()
        .limjangId(limjang.getLimjangId())
        .checkListVersion(checkListVersion)
        .images(urlList)
        .nickname(limjang.getNickname())
        .purposeCode(limjang.getPurpose().getValue())
        .priceType(priceType.getValue())
        .priceList(priceList)
        .address(limjang.getAddress())
        .addressDetail(limjang.getAddressDetail())
        .createdAt(limjang.getCreatedAt())
        .updatedAt(limjang.getUpdatedAt())
        .build();
  }
}
