package umc.th.juinjang.api.limjang.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import umc.th.juinjang.domain.limjang.model.Address;
import umc.th.juinjang.domain.limjang.model.LimjangPrice;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPurpose;
import umc.th.juinjang.domain.limjang.repository.NotePriceFactory;

public record NotePatchRequestV2(
	@NotNull
	LimjangPriceType priceType,

	@Pattern(regexp = "^[0-9]+$", message = "가격은 숫자만 입력해야 합니다.")
	String price,
	@Pattern(regexp = "^[0-9]+$", message = "가격은 숫자만 입력해야 합니다.")
	String monthlyRent,

	String roadAddress,
	String addressDetail,

	String bcode,

	String nickname,

	String floor,
	Integer pyong, //null 처리를 위해 Integer로 변경
	String sido,
	String sigungu,
	String bname1,
	String bname2
) {

	public LimjangPrice toUpdatedPrice(LimjangPurpose purpose) {
		if (price == null && monthlyRent == null) {
			return LimjangPrice.empty();
		}
		return NotePriceFactory.create(purpose, priceType, price, monthlyRent);
	}

	public Address toUpdatedAddress() {
		if (isAddressAllEmpty()) {
			return Address.empty();
		}
		return Address.create(roadAddress, addressDetail, bcode, sido, sigungu, bname1, bname2);
	}

	private boolean isAddressAllEmpty() {
		return isBlank(roadAddress)
			&& isBlank(addressDetail)
			&& isBlank(bcode)
			&& isBlank(sido)
			&& isBlank(sigungu)
			&& isBlank(bname1)
			&& isBlank(bname2);
	}

	private boolean isBlank(String s) {
		return s == null || s.isBlank();
	}
}
