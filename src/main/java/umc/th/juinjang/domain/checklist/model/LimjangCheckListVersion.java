package umc.th.juinjang.domain.checklist.model;

import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.limjang.model.LimjangPurpose;

public enum LimjangCheckListVersion {
  LIMJANG,
  NON_LIMJANG;

  public static LimjangCheckListVersion getByLimjangType(Limjang limjang) {
    LimjangPropertyType propertyType = limjang.getPropertyType();

    if (limjang.getPurpose() == LimjangPurpose.RESIDENTIAL_PURPOSE && (propertyType == LimjangPropertyType.VILLA || propertyType == LimjangPropertyType.OFFICE_TEL)) {
      return LimjangCheckListVersion.NON_LIMJANG;
    }
    return LimjangCheckListVersion.LIMJANG;
  }
}