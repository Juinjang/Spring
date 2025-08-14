package umc.th.juinjang.api.termsAgreement.service;

import static umc.th.juinjang.common.code.status.ErrorStatus.*;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.common.exception.handler.MemberHandler;
import umc.th.juinjang.domain.termsAgreement.repository.TermsAgreement;
import umc.th.juinjang.domain.termsAgreement.repository.TermsAgreementRepository;
import umc.th.juinjang.domain.termsAgreement.repository.TermsType;

@Service
@RequiredArgsConstructor
public class TermsAgreementService {

	private final TermsAgreementRepository termsAgreementRepository;

	public boolean checkStatus(Long memberId, TermsType termsType) {
		return termsAgreementRepository.findByMemberIdAndTermsType(memberId, termsType)
			.isPresent();
	}

	public void agreeToSpecificTerms(Long memberId, TermsType termsType) {
		Optional<TermsAgreement> existingAgreement =
			termsAgreementRepository.findByMemberIdAndTermsType(memberId, termsType);

		if (existingAgreement.isPresent()) {
			throw new MemberHandler(TERMS_AGREEMENT_DUPLICATED);
		}

		TermsAgreement agreement = TermsAgreement.create(memberId, termsType);
		termsAgreementRepository.save(agreement);
	}
}
