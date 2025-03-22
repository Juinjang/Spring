package umc.th.juinjang.domain.limjang.model;

import java.util.Arrays;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.LimjangHandler;

public enum LimjangPriceType {
  SALE(0), // 매매
  PULL_RENT(1), // 전세
  MONTHLY_RENT(2), //월세

  MARKET_PRICE(3); // 실거래가


  private final int value;

  LimjangPriceType(int value) {
    this.value = value;
  }

  // 숫자 리턴
  public int getValue() {
    return value;
  }

  public static LimjangPriceType find(int inputValue) {
    return Arrays.stream(LimjangPriceType.values())
        .filter(it -> it.value == inputValue)
        .findAny()
        .orElseThrow(() -> new LimjangHandler(ErrorStatus.LIMJANG_POST_TYPE_ERROR));
  }
}
