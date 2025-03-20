package umc.th.juinjang.api.limjang.service;

import java.util.ArrayList;
import java.util.List;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.LimjangHandler;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.model.LimjangPrice;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPurpose;

public class LimjangPriceBridge {
  public static LimjangPrice determineLimjangPrice(List<String> priceList, Integer purpose, Integer priceType){
    checkExpectedSize(priceType, priceList.size());
    if (purpose == 0){ // 부동산 투자 목적 -> 실거래가
      return LimjangPrice.builder().marketPrice(priceList.get(0)).build();
    } else if (purpose == 1){ // 직접 거래 목적
      switch (priceType){
        case 0 : // 매매
          return LimjangPrice.builder().sellingPrice(priceList.get(0)).build();
        case 1 :// 전세
          return LimjangPrice.builder().pullRent(priceList.get(0)).build();
        case 2 : // 월세 : 0, 보증금 : 1 이 경우 배열 길이는 무조건 2여야만 함.
          return LimjangPrice.builder().depositPrice(priceList.get(0))
              .monthlyRent(priceList.get(1)).build();
        case 3 :
          return LimjangPrice.builder().marketPrice(priceList.get(0)).build();
      }
    }
    return null;
  }

  public static List<String> makePriceList(
       Integer priceType, Integer purpose, LimjangPrice limjangPrice
  ){
    List<String> priceList = new ArrayList<>();

    if (purpose == 0){ // 부동산 투자 목적 -> 실거래가
      priceList.add(limjangPrice.getMarketPrice());
    } else if (purpose == 1){ // 직접 거래 목적
      switch (priceType){
        case 0 : // 매매
          priceList.add(limjangPrice.getSellingPrice());
          break;
        case 1 :// 전세
            priceList.add(limjangPrice.getPullRent());
            break;
        case 2 :
          priceList.add(limjangPrice.getDepositPrice());
          priceList.add(limjangPrice.getMonthlyRent());
          break;
        case 3 :
          priceList.add(limjangPrice.getMarketPrice());
          break;
      }
    }
    return priceList;
  }

  public static List<String> makePriceListVersion2(
      LimjangPriceType priceType,
      LimjangPurpose purpose,
      LimjangPrice limjangPrice
  ){
    List<String> priceList = new ArrayList<>();

    if (purpose == LimjangPurpose.INVESTMENT){ // 부동산 투자 목적 -> 실거래가
      priceList.add(limjangPrice.getMarketPrice());
    } else if (purpose == LimjangPurpose.RESIDENTIAL_PURPOSE){ // 직접 거래 목적
      switch (priceType){
        case SALE: // 매매
          priceList.add(limjangPrice.getSellingPrice());
          break;
        case PULL_RENT :// 전세
          priceList.add(limjangPrice.getPullRent());
          break;
        case MONTHLY_RENT:
          priceList.add(limjangPrice.getDepositPrice());
          priceList.add(limjangPrice.getMonthlyRent());
          break;
        case MARKET_PRICE:
          priceList.add(limjangPrice.getMarketPrice());
          break;
      }
    }
    return priceList;
  }

  public static void checkExpectedSize(int priceType, int priceListSize){
    int expectedSize = (priceType == 2) ? 2 : 1; // 월세의 경우 배열길이 2
    if (priceListSize != expectedSize) {
      throw new LimjangHandler(ErrorStatus.LIMJANG_POST_PRICE_ERROR);
    }
  }

  public static String getPriceToString(Limjang limjang) {
    List<String> priceList = makePriceListVersion2(limjang.getPriceType(),limjang.getPurpose(), limjang.getLimjangPrice());
    return (limjang.getPriceType().getValue() == 2) ? priceList.get(1) : priceList.get(0);
  }
}