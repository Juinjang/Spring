package umc.th.juinjang.api.note.liked.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.dto.ApiResponse;
import umc.th.juinjang.api.note.liked.service.LikedNoteCommandService;
import umc.th.juinjang.api.note.liked.service.response.LikedNoteDeleteResponse;
import umc.th.juinjang.api.note.liked.service.response.LikedNotePostResponse;
import umc.th.juinjang.domain.member.model.Member;

@RestController
@RequestMapping("/api/v2/shared-notes")
@RequiredArgsConstructor
public class LikedNoteController {

	private final LikedNoteCommandService likedNoteCommandService;

	@Operation(summary = "공유노트 좋아요 등록 API")
	@PostMapping("/{sharedNoteId}/likes")
	public ApiResponse<LikedNotePostResponse> createLikedNote(@AuthenticationPrincipal Member member,
		@PathVariable("sharedNoteId") Long sharedNoteId) {
		return ApiResponse.onSuccess(likedNoteCommandService.createLikedNote(member, sharedNoteId));
	}

	@Operation(summary = "공유노트 좋아요 취소 API")
	@DeleteMapping("/{sharedNoteId}/likes")
	public ApiResponse<LikedNoteDeleteResponse> deleteLikedNote(@AuthenticationPrincipal Member member,
		@PathVariable("sharedNoteId") Long sharedNoteId) {
		return ApiResponse.onSuccess(likedNoteCommandService.deleteLikedNote(member, sharedNoteId));
	}
}
