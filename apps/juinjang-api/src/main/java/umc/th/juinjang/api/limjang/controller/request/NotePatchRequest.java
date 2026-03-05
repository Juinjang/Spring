package umc.th.juinjang.api.limjang.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import umc.th.juinjang.domain.limjang.model.Address;
import umc.th.juinjang.domain.limjang.model.LimjangPrice;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.limjang.model.LimjangPurpose;
import umc.th.juinjang.domain.limjang.repository.NotePriceFactory;

public record NotePatchRequest(
	@NotNull
	LimjangPriceType priceType,
	@NotBlank
	@Pattern(regexp = "^[0-9]+$", message = "가격은 숫자만 입력해야 합니다.")
	String price,
	@Pattern(regexp = "^[0-9]+$", message = "가격은 숫자만 입력해야 합니다.")
	String monthlyRent,
	@NotBlank
	String roadAddress,
	String addressDetail,
	@NotBlank
	String bcode,
	@NotBlank
	String nickname,
	@NotBlank
	String floor,
	int pyong,
	String sido,
	String sigungu,
	String bname1,
	String bname2
) {

	public LimjangPrice toUpdatedPrice(LimjangPurpose purpose) {
		return NotePriceFactory.create(purpose, priceType, price, monthlyRent);
	}

	public Address toUpdatedAddress() {
		return Address.create(roadAddress, addressDetail, bcode, sido, sigungu, bname1, bname2);
	}
}
