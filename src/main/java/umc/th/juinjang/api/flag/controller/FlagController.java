package umc.th.juinjang.api.flag.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.dto.ApiResponse;
import umc.th.juinjang.api.flag.controller.request.FlagSharedNotePostRequest;
import umc.th.juinjang.api.flag.service.FlagSharedNoteCommandService;
import umc.th.juinjang.domain.member.model.Member;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
public class FlagController {

	private final FlagSharedNoteCommandService flagSharedNoteCommandService;

	@Operation(summary = "노트 신고하기 API")
	@PostMapping("/reports/shared-note")
	public ApiResponse<Void> findUsersSharedNotes(@AuthenticationPrincipal Member member,
		@RequestBody FlagSharedNotePostRequest flagSharedNotePostRequest
	) {
		flagSharedNoteCommandService.createSharedNoteFlag(member, flagSharedNotePostRequest);
		return ApiResponse.onSuccess(null);
	}
}
