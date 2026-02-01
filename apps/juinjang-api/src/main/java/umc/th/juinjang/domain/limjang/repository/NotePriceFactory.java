package umc.th.juinjang.domain.limjang.repository;

import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.LimjangHandler;
import umc.th.juinjang.domain.limjang.model.LimjangPrice;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPurpose;

public class NotePriceFactory {
	public static LimjangPrice create(LimjangPurpose purpose, LimjangPriceType priceType, String price,
		String monthlyRent) {
		checkValidPriceTypeAndPrice(priceType, monthlyRent);
		if (purpose == LimjangPurpose.INVESTMENT) {
			return createInvestmentPrice(price);
		} else {
			return createResidencePrice(priceType, price, monthlyRent);
		}
	}

	private static void checkValidPriceTypeAndPrice(LimjangPriceType priceType, String monthlyRent) {
		if (priceType == LimjangPriceType.MONTHLY_RENT && monthlyRent == null) {
			throw new LimjangHandler(ErrorStatus.LIMJANG_POST_PRICE_ERROR);
		}

		if (priceType != LimjangPriceType.MONTHLY_RENT && monthlyRent != null) {
			throw new LimjangHandler(ErrorStatus.LIMJANG_POST_PRICE_ERROR);
		}
	}

	private static LimjangPrice createInvestmentPrice(String price) {
		return LimjangPrice.builder()
			.marketPrice(price)
			.build();
	}

	private static LimjangPrice createResidencePrice(LimjangPriceType priceType, String price, String monthlyRent) {
		return switch (priceType) {
			case SALE -> LimjangPrice.builder()
				.sellingPrice(price)
				.build();
			case PULL_RENT -> LimjangPrice.builder()
				.pullRent(price)
				.build();
			case MONTHLY_RENT -> LimjangPrice.builder()
				.depositPrice(price)
				.monthlyRent(monthlyRent)
				.build();
			case MARKET_PRICE -> LimjangPrice.builder()
				.marketPrice(price)
				.build();
		};
	}
}
