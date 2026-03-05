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
        // 둘 다 없음 -> 삭제(주소 null 처리 유도)
        if (isDeleteAddressIntent()) {
            return Address.empty();
        }

        // 상세주소만 -> 정책상 금지
        if (isDetailOnly()) {
            throw new IllegalArgumentException("본주소 없이 상세주소만 입력할 수 없습니다.");
        }

        // 본주소가 있으면 생성
        return Address.create(roadAddress, addressDetail, bcode, sido, sigungu, bname1, bname2);
    }

	private boolean isBlank(String s) {
		return s == null || s.isBlank();
	}

    private boolean isDeleteAddressIntent() { // 4번
        return isBlank(roadAddress) && isBlank(addressDetail);
    }

    private boolean isDetailOnly() { // 3번
        return isBlank(roadAddress) && !isBlank(addressDetail);
    }
}
