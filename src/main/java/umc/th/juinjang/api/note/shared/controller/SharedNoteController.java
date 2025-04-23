package umc.th.juinjang.api.note.shared.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.dto.ApiResponse;
import umc.th.juinjang.api.note.shared.service.SharedNoteCommandService;
import umc.th.juinjang.api.note.shared.service.SharedNoteQueryService;
import umc.th.juinjang.api.note.shared.service.response.SharedNoteGetResponse;
import umc.th.juinjang.domain.member.model.Member;

@RestController
@RequestMapping("/api/v2/shared-notes")
@RequiredArgsConstructor
public class SharedNoteController {

	private final SharedNoteCommandService sharedNoteCommandService;
	private final SharedNoteQueryService sharedNoteQueryService;

	@Operation(summary = "노트 구매 API")
	@PostMapping("/{sharedNoteId}/purchase")
	public ApiResponse<Void> createSharedNotePurchase(@AuthenticationPrincipal Member member,
		@PathVariable("sharedNoteId") Long sharedNoteId) {
		sharedNoteCommandService.createSharedNotePurchase(member, sharedNoteId);
		return ApiResponse.onSuccess(null);
	}

	@Operation(summary = "공유 노트 상세보기 API")
	@GetMapping("/{sharedNoteId}")
	public ApiResponse<SharedNoteGetResponse> findSharedNote(@AuthenticationPrincipal Member member,
		@PathVariable("sharedNoteId") Long sharedNoteId) {
		return ApiResponse.onSuccess(sharedNoteQueryService.findSharedNote(member, sharedNoteId));
	}

}
