package umc.th.juinjang.api.member.controller;

import static umc.th.juinjang.common.code.status.ErrorStatus.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.dto.ApiResponse;
import umc.th.juinjang.api.member.controller.request.IntroductionPatchRequest;
import umc.th.juinjang.api.member.controller.request.MemberAgreeVersionPostRequest;
import umc.th.juinjang.api.member.controller.request.MemberRequestDto;
import umc.th.juinjang.api.member.service.MemberService;
import umc.th.juinjang.api.member.service.response.MemberResponseDto;
import umc.th.juinjang.common.ExceptionHandler;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.domain.member.model.Member;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class MemberController {

	private final MemberService memberService;

	@CrossOrigin
	@Operation(summary = "닉네임 설정")
	@PatchMapping("/nickname")
	public ApiResponse<MemberResponseDto.nicknameDto> patchNickname(@AuthenticationPrincipal Member member,
		@RequestBody MemberRequestDto memberRequestDto) {
		if (!memberRequestDto.getNickname().isEmpty()) {
			MemberResponseDto.nicknameDto result = memberService.patchNickname(member, memberRequestDto);
			return ApiResponse.onSuccess(result);
		} else
			throw new ExceptionHandler(NICKNAME_EMPTY);
	}

	@CrossOrigin
	@Operation(summary = "프로필 조회")
	@GetMapping("/profile")
	public ApiResponse<MemberResponseDto.profileDto> getProfile(@AuthenticationPrincipal Member member) {
		MemberResponseDto.profileDto result = memberService.getProfile(member);
		return ApiResponse.onSuccess(result);
	}

	@CrossOrigin
	@Operation(summary = "프로필 이미지 수정")
	@PatchMapping("/profile/image")
	public ApiResponse<MemberResponseDto.profileDto> getProfile(@AuthenticationPrincipal Member member,
		@RequestPart MultipartFile multipartFile) {
		if (multipartFile == null || multipartFile.isEmpty())
			throw new ExceptionHandler(ErrorStatus.IMAGE_EMPTY);
		MemberResponseDto.profileDto result = memberService.updateProfileImage(member, multipartFile);
		return ApiResponse.onSuccess(result);
	}

	@Operation(summary = "한줄 소개 변경")
	@PatchMapping("/profile/introduction")
	public ApiResponse<IntroductionPatchRequest> updateIntroduction(@AuthenticationPrincipal Member member,
		@RequestBody IntroductionPatchRequest request) {
		memberService.updateIntroduction(member, request.getIntroduction());
		return ApiResponse.onSuccess(null);
	}

	@Operation(summary = "약관 동의 버전 전송")
	@PatchMapping("/members/terms")
	public ApiResponse<Void> createMemberAgreeVersion(@AuthenticationPrincipal Member member,
		@RequestBody @Valid MemberAgreeVersionPostRequest memberAgreeVersionPostRequest) {
		memberService.createMemberAgreeVersion(member, memberAgreeVersionPostRequest);
		return ApiResponse.onSuccess(null);
	}

}
