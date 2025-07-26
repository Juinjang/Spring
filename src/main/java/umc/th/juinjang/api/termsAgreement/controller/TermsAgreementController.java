package umc.th.juinjang.api.termsAgreement.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.dto.ApiResponse;
import umc.th.juinjang.api.termsAgreement.controller.request.AgreeRequest;
import umc.th.juinjang.api.termsAgreement.controller.response.AgreeResponse;
import umc.th.juinjang.api.termsAgreement.controller.response.StatusResponse;
import umc.th.juinjang.api.termsAgreement.service.TermsAgreementService;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.termsAgreement.repository.TermsType;

@RestController
@RequestMapping("/api/v2/terms-agreement")
@RequiredArgsConstructor
public class TermsAgreementController {

	private final TermsAgreementService termsAgreementService;

	@Operation(summary = "특정 약관 동의 여부 확인",
		description = "로그인한 멤버가 특정 약관에 동의했는지 확인합니다.")
	@GetMapping("/{termsType}")
	public ApiResponse<StatusResponse> checkStatus(
		@AuthenticationPrincipal Member member,
		@Parameter(description = "확인할 약관 타입")
		@PathVariable TermsType termsType) {
		boolean isAgreed = termsAgreementService.checkStatus(member.getMemberId(), termsType);
		return ApiResponse.onSuccess(StatusResponse.of(isAgreed));
	}

	@Operation(summary = "특정 약관에 동의 하기",
		description = "해당 멤버가 특정 약관에 동의합니다.")
	@PostMapping
	public ApiResponse<AgreeResponse> agreeToSpecificTerms(
		@AuthenticationPrincipal Member member,
		@RequestBody AgreeRequest request
	) {
		termsAgreementService.agreeToSpecificTerms(member.getMemberId(), request.termsType());
		return ApiResponse.onSuccess(AgreeResponse.from(request.termsType(), true));
	}

}
