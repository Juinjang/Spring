package umc.th.juinjang.api.sharednote.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.dto.ApiResponse;
import umc.th.juinjang.api.limjang.controller.request.LimjangPostRequest;
import umc.th.juinjang.api.limjang.service.response.LimjangPostResponse;
import umc.th.juinjang.api.sharednote.service.SharedNoteCommandService;
import umc.th.juinjang.domain.member.model.Member;

@RestController
@RequestMapping("/api/v2/shared-notes")
@RequiredArgsConstructor
public class SharedNoteController {

	private final SharedNoteCommandService sharedNoteCommandService;

	@Operation(summary = "노트 구매 API")
	@PostMapping("/{sharedNoteId}/purchase")
	public ApiResponse<Void> createSharedNotePurchase(@AuthenticationPrincipal Member member,
		@PathVariable("sharedNoteId") Long sharedNoteId) {
		sharedNoteCommandService.createSharedNotePurchase(member, sharedNoteId);
		return ApiResponse.onSuccess(null);
	}

}
