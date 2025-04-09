package umc.th.juinjang.api.limjang.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.dto.ApiResponse;
import umc.th.juinjang.api.limjang.controller.parameter.LimjangSortOptions;
import umc.th.juinjang.api.limjang.controller.request.NotePatchRequest;
import umc.th.juinjang.api.limjang.controller.request.NotePostRequest;
import umc.th.juinjang.api.limjang.service.NoteCommandServiceV2;
import umc.th.juinjang.api.limjang.service.NoteQueryServiceV2;
import umc.th.juinjang.api.limjang.service.response.UserNotesGetResponse;
import umc.th.juinjang.common.code.status.SuccessStatus;
import umc.th.juinjang.domain.member.model.Member;

@RestController
@RequestMapping("/api/v2/notes")
@RequiredArgsConstructor
public class NoteControllerV2 {

	private final NoteCommandServiceV2 noteCommandService;
	private final NoteQueryServiceV2 noteQueryService;

	@Operation(summary = "임장 생성 API V2")
	@PostMapping
	public ApiResponse<Void> createNote(@RequestBody @Valid NotePostRequest request,
		@AuthenticationPrincipal Member member) {
		noteCommandService.createNote(request, member);
		return ApiResponse.of(SuccessStatus._CREATED, null);
	}

	@Operation(summary = "마이 노트 조회 API V2")
	@GetMapping
	public ApiResponse<UserNotesGetResponse> findUsersNotes(
		@RequestParam("sort") LimjangSortOptions sortOptions,
		@AuthenticationPrincipal Member member) {
		return ApiResponse.onSuccess(noteQueryService.findUsersNotes(member, sortOptions));
	}

	@Operation(summary = "임장 수정 API V2")
	@PatchMapping("/{noteId}")
	public ApiResponse<Void> updateNote(@PathVariable(name = "noteId") Long noteId,
		@RequestBody @Valid NotePatchRequest request,
		@AuthenticationPrincipal Member member) {
		noteCommandService.updateNote(noteId, request);
		return ApiResponse.onSuccess(null);
	}
}
