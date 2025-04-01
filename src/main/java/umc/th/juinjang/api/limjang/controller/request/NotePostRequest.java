package umc.th.juinjang.api.limjang.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.limjang.model.LimjangPurpose;

public record NotePostRequest(
	@NotNull
	LimjangPurpose purposeType,
	@NotNull
	LimjangPropertyType propertyType,
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
}
