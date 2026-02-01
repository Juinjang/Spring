package umc.th.juinjang.api.termsAgreement.controller.response;

import umc.th.juinjang.domain.termsAgreement.repository.TermsType;

public record AgreeResponse(
	TermsType termsType,
	boolean isAgreed
) {
	public static AgreeResponse from(TermsType termsType, boolean isAgreed) {
		return new AgreeResponse(
			termsType,
			isAgreed
		);
	}
}
