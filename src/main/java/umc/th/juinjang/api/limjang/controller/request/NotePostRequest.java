package umc.th.juinjang.api.limjang.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.LimjangHandler;
import umc.th.juinjang.domain.limjang.model.Address;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.model.LimjangPrice;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.limjang.model.LimjangPurpose;
import umc.th.juinjang.domain.limjang.repository.NotePriceFactory;
import umc.th.juinjang.domain.member.model.Member;

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
	public Limjang toEntity(Member member) {
		LimjangPrice limjangPrice = NotePriceFactory.create(purposeType, priceType, price, monthlyRent);
		Address address = Address.create(roadAddress, addressDetail, bcode, sido, sigungu, bname1, bname2);

		return Limjang.create(member, limjangPrice, purposeType, propertyType, priceType, nickname, address, pyong,
			floor);
	}
}
