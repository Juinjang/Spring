package umc.th.juinjang.api.pencil.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.apple.itunes.storekit.model.AccountTenure;
import com.apple.itunes.storekit.model.ConsumptionRequest;
import com.apple.itunes.storekit.model.ConsumptionStatus;
import com.apple.itunes.storekit.model.DeliveryStatus;
import com.apple.itunes.storekit.model.LifetimeDollarsPurchased;
import com.apple.itunes.storekit.model.LifetimeDollarsRefunded;
import com.apple.itunes.storekit.model.Platform;
import com.apple.itunes.storekit.model.PlayTime;
import com.apple.itunes.storekit.model.RefundPreference;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.api.pencil.service.response.PurchasedPencilResponse;
import umc.th.juinjang.api.pencil.service.response.UsedPencilResponse;
import umc.th.juinjang.api.pencilAccount.service.PencilAccountFinder;
import umc.th.juinjang.common.exception.handler.PencilAccountHandler;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.pencil.purchased.model.PurchasedPencil;
import umc.th.juinjang.domain.pencil.used.model.UsedPencil;
import umc.th.juinjang.domain.pencilaccount.model.PencilAccount;

@Slf4j
@Service
@RequiredArgsConstructor
public class PencilQueryService {

	private static final double DOLLAR_EXCHANGE_RATE = 1374.0;
	private final AcquiredPencilFinder acquiredPencilFinder;
	private final PurchasedPencilFinder purchasedPencilFinder;
	private final UsedPencilFinder usedPencilFinder;
	private final PencilAccountFinder pencilAccountFinder;

	public List<PurchasedPencilResponse> getPurchasedPencils(Member member) {
		List<PurchasedPencil> purchasedPencils = purchasedPencilFinder.findAllByMemberWhereDeliverySuccessOrderByCreatedAtDesc(
			member);
		return purchasedPencils.stream()
			.map(PurchasedPencilResponse::from)
			.toList();
	}

	public List<UsedPencilResponse> getUsedPencils(Member member) {
		List<UsedPencil> usedPencils = usedPencilFinder.findAllByMemberOrderByCreatedAtDesc(member);
		return usedPencils.stream()
			.map(UsedPencilResponse::from)
			.toList();
	}

	public ConsumptionRequest getConsumptionRequest(String transactionId) {
		return converterToConsumptionRequest(purchasedPencilFinder.findByTransactionId(transactionId));
	}

	private ConsumptionRequest converterToConsumptionRequest(Optional<PurchasedPencil> purchasedPencil) {
		if (purchasedPencil.isPresent()) {
			PurchasedPencil purchase = purchasedPencil.get();
			Member member = purchase.getMember();

			ConsumptionRequest request = new ConsumptionRequest();
			request.setCustomerConsented(true);
			request.setPlayTime(calculatePlayTime(purchase.getPlayTime()));
			request.setAppAccountToken(purchase.getAppAccountToken());
			request.setDeliveryStatus(DeliveryStatus.fromValue(purchase.getDeliveryStatus().getAppleCode()));
			request.setConsumptionStatus(
				converterToConsumptionStatus(purchase.getPurchaseQuantity(), purchase.getRemainQuantity()));
			request.setAccountTenure(calculateAccountTenure(member));
			request.setLifetimeDollarsPurchased(calculateLifeDollarPurchased(member));
			request.setLifetimeDollarsRefunded(calculateLifeDollarRefunded(member));
			request.setPlatform(Platform.APPLE);
			request.setSampleContentProvided(getSampleContentProvided(member));
			// request.setUserStatusgetUserStatus(member));
			request.setRefundPreference(RefundPreference.PREFER_GRANT);
			log.info("getConsumptionRequest : {}", request);

			return request;
		}
		return null;
	}

	private LifetimeDollarsPurchased calculateLifeDollarPurchased(Member member) {
		try {
			PencilAccount buyerAccount = pencilAccountFinder.findByMember(member);
			long totalPrice = buyerAccount.getTotalPurchaseAmount() - buyerAccount.getTotalRefundAmount();

			if (totalPrice == 0L) {
				return LifetimeDollarsPurchased.ZERO_DOLLARS;
			}

			double usdAmount = totalPrice / DOLLAR_EXCHANGE_RATE;

			if (usdAmount <= 0.0) {
				return LifetimeDollarsPurchased.ZERO_DOLLARS;
			} else if (usdAmount < 50) {
				return LifetimeDollarsPurchased.ONE_CENT_TO_FORTY_NINE_DOLLARS_AND_NINETY_NINE_CENTS;
			} else if (usdAmount < 100) {
				return LifetimeDollarsPurchased.FIFTY_DOLLARS_TO_NINETY_NINE_DOLLARS_AND_NINETY_NINE_CENTS;
			} else if (usdAmount < 500) {
				return LifetimeDollarsPurchased.ONE_HUNDRED_DOLLARS_TO_FOUR_HUNDRED_NINETY_NINE_DOLLARS_AND_NINETY_NINE_CENTS;
			} else if (usdAmount < 1000) {
				return LifetimeDollarsPurchased.FIVE_HUNDRED_DOLLARS_TO_NINE_HUNDRED_NINETY_NINE_DOLLARS_AND_NINETY_NINE_CENTS;
			} else if (usdAmount < 2000) {
				return LifetimeDollarsPurchased.ONE_THOUSAND_DOLLARS_TO_ONE_THOUSAND_NINE_HUNDRED_NINETY_NINE_DOLLARS_AND_NINETY_NINE_CENTS;
			} else {
				return LifetimeDollarsPurchased.TWO_THOUSAND_DOLLARS_OR_GREATER;
			}
		} catch (PencilAccountHandler exception) {
			return LifetimeDollarsPurchased.UNDECLARED;
		}
	}

	private LifetimeDollarsRefunded calculateLifeDollarRefunded(Member member) {
		try {
			PencilAccount buyerAccount = pencilAccountFinder.findByMember(member);
			long totalRefundWon = buyerAccount.getTotalRefundAmount();

			if (totalRefundWon == 0L) {
				return LifetimeDollarsRefunded.ZERO_DOLLARS;
			}

			double usdAmount = totalRefundWon / DOLLAR_EXCHANGE_RATE;

			if (usdAmount <= 0.0) {
				return LifetimeDollarsRefunded.ZERO_DOLLARS;
			} else if (usdAmount < 50) {
				return LifetimeDollarsRefunded.ONE_CENT_TO_FORTY_NINE_DOLLARS_AND_NINETY_NINE_CENTS;
			} else if (usdAmount < 100) {
				return LifetimeDollarsRefunded.FIFTY_DOLLARS_TO_NINETY_NINE_DOLLARS_AND_NINETY_NINE_CENTS;
			} else if (usdAmount < 500) {
				return LifetimeDollarsRefunded.ONE_HUNDRED_DOLLARS_TO_FOUR_HUNDRED_NINETY_NINE_DOLLARS_AND_NINETY_NINE_CENTS;
			} else if (usdAmount < 1000) {
				return LifetimeDollarsRefunded.FIVE_HUNDRED_DOLLARS_TO_NINE_HUNDRED_NINETY_NINE_DOLLARS_AND_NINETY_NINE_CENTS;
			} else if (usdAmount < 2000) {
				return LifetimeDollarsRefunded.ONE_THOUSAND_DOLLARS_TO_ONE_THOUSAND_NINE_HUNDRED_NINETY_NINE_DOLLARS_AND_NINETY_NINE_CENTS;
			} else {
				return LifetimeDollarsRefunded.TWO_THOUSAND_DOLLARS_OR_GREATER;
			}
		} catch (PencilAccountHandler exception) {
			return LifetimeDollarsRefunded.UNDECLARED;
		}

	}

	private boolean getSampleContentProvided(Member member) {
		return acquiredPencilFinder.existsByMemberId(member.getMemberId());
	}

	private ConsumptionStatus converterToConsumptionStatus(Long purchaseQuantity, Long remainQuantity) {
		if (remainQuantity == null || purchaseQuantity == null) {
			return ConsumptionStatus.UNDECLARED;
		}

		if (remainQuantity.equals(purchaseQuantity)) {
			return ConsumptionStatus.NOT_CONSUMED;
		} else if (remainQuantity == 0) {
			return ConsumptionStatus.FULLY_CONSUMED;
		} else if (remainQuantity > 0) {
			return ConsumptionStatus.PARTIALLY_CONSUMED;
		}

		return ConsumptionStatus.UNDECLARED;
	}

	private AccountTenure calculateAccountTenure(Member member) {
		// 회원 가입일로부터 현재까지의 기간을 계산
		LocalDateTime memberCreatedAt = member.getCreatedAt();
		LocalDateTime now = LocalDateTime.now();
		long daysBetween = ChronoUnit.DAYS.between(memberCreatedAt, now);

		// 기간에 따라 AccountTenure 반환
		if (daysBetween <= 3) {
			return AccountTenure.ZERO_TO_THREE_DAYS;
		} else if (daysBetween <= 10) {
			return AccountTenure.THREE_DAYS_TO_TEN_DAYS;
		} else if (daysBetween <= 30) {
			return AccountTenure.TEN_DAYS_TO_THIRTY_DAYS;
		} else if (daysBetween <= 90) {
			return AccountTenure.THIRTY_DAYS_TO_NINETY_DAYS;
		} else if (daysBetween <= 180) {
			return AccountTenure.NINETY_DAYS_TO_ONE_HUNDRED_EIGHTY_DAYS;
		} else if (daysBetween <= 365) {
			return AccountTenure.ONE_HUNDRED_EIGHTY_DAYS_TO_THREE_HUNDRED_SIXTY_FIVE_DAYS;
		} else {
			return AccountTenure.GREATER_THAN_THREE_HUNDRED_SIXTY_FIVE_DAYS;
		}
	}

	private PlayTime calculatePlayTime(Integer playTime) {
		if (playTime == null || playTime < 0) {
			return PlayTime.UNDECLARED;
		}

		if (playTime <= 5) {
			return PlayTime.ZERO_TO_FIVE_MINUTES;
		} else if (playTime <= 60) {
			return PlayTime.FIVE_TO_SIXTY_MINUTES;
		} else if (playTime <= 360) { // 6시간
			return PlayTime.ONE_TO_SIX_HOURS;
		} else if (playTime <= 1440) { // 24시간
			return PlayTime.SIX_HOURS_TO_TWENTY_FOUR_HOURS;
		} else if (playTime <= 5760) { // 4일
			return PlayTime.ONE_DAY_TO_FOUR_DAYS;
		} else if (playTime <= 23040) { // 16일
			return PlayTime.FOUR_DAYS_TO_SIXTEEN_DAYS;
		} else {
			return PlayTime.OVER_SIXTEEN_DAYS;
		}
	}

}
