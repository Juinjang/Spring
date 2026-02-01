package umc.th.juinjang.api.termsAgreement.controller.request;

import umc.th.juinjang.domain.termsAgreement.repository.TermsType;

public record AgreeRequest(
	TermsType termsType
) {
}
