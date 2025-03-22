package umc.th.juinjang.domain.limjang.model;

import java.util.Arrays;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.LimjangHandler;

// 매물유형
public enum LimjangPropertyType {
  APARTMENT(0), // 아파트
  VILLA(1),// 빌라
  OFFICE_TEL(2), // 오피스텔
  DETACHED_HOUSE(3); // 단독주택

  private final int value;

  LimjangPropertyType(int value) {
    this.value = value;
  }

  // 숫자 리턴
  public int getValue() {
    return value;
  }

  public static LimjangPropertyType find(int inputValue) {
    return Arrays.stream(LimjangPropertyType.values())
        .filter(it -> it.value == inputValue)
        .findAny()
        .orElseThrow(() -> new LimjangHandler(ErrorStatus.LIMJANG_POST_TYPE_ERROR));
  }
}
