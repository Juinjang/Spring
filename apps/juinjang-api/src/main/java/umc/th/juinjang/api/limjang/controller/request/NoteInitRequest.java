package umc.th.juinjang.api.limjang.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.model.LimjangPrice;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.limjang.model.LimjangPurpose;
import umc.th.juinjang.domain.limjang.repository.NotePriceFactory;
import umc.th.juinjang.domain.member.model.Member;

public record NoteInitRequest(
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
	String monthlyRent
) {
	public Limjang toEntity(Member member) {
		LimjangPrice limjangPrice = NotePriceFactory.create(purposeType, priceType, price, monthlyRent);

		return Limjang.initNote(member, limjangPrice, purposeType, propertyType, priceType);
	}
}
