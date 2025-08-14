package umc.th.juinjang.domain.limjang.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.th.juinjang.domain.common.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LimjangPrice extends BaseEntity {

	@Id
	@Column(name = "price_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long priceId;

	private String marketPrice;

	private String sellingPrice;

	private String depositPrice;

	private String monthlyRent;

	private String pullRent;

	@OneToOne(mappedBy = "limjangPrice", cascade = CascadeType.ALL, orphanRemoval = true)
	private Limjang limjang;

	public void updateLimjangPrice(LimjangPrice newLimjangPrice) {
		this.marketPrice = newLimjangPrice.getMarketPrice();
		this.sellingPrice = newLimjangPrice.getSellingPrice();
		this.depositPrice = newLimjangPrice.getDepositPrice();
		this.monthlyRent = newLimjangPrice.getMonthlyRent();
		this.pullRent = newLimjangPrice.getPullRent();
	}

	public String getPrice(LimjangPriceType priceType, LimjangPurpose purpose) {
		if (purpose == LimjangPurpose.INVESTMENT) {
			return this.getMarketPrice();
		} else if (purpose == LimjangPurpose.RESIDENTIAL_PURPOSE) {
			return switch (priceType) {
				case SALE -> this.getSellingPrice();
				case PULL_RENT -> this.getPullRent();
				case MONTHLY_RENT -> this.getDepositPrice();
				case MARKET_PRICE -> this.getMarketPrice();
			};
		}
		return null;
	}
}
