package umc.th.juinjang.api.pencil.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.dto.ApiResponse;
import umc.th.juinjang.api.pencil.service.PencilCommandService;
import umc.th.juinjang.api.pencil.service.PencilQueryService;
import umc.th.juinjang.api.pencil.service.response.AcquiredPencilResponse;
import umc.th.juinjang.api.pencil.service.response.PurchasedPencilResponse;
import umc.th.juinjang.api.pencil.service.response.UsedPencilResponse;
import umc.th.juinjang.domain.member.model.Member;

@RestController
@RequestMapping("/api/v2/pencil")
@RequiredArgsConstructor
public class PencilController {

	private final PencilQueryService pencilQueryService;
	private final PencilCommandService pencilCommandService;

	@Operation(summary = "얻은 연필 목록을 불러온다.")
	@GetMapping("/acquired")
	public ApiResponse<List<AcquiredPencilResponse>> getAcquiredPencilHistory(@AuthenticationPrincipal Member member) {
		return ApiResponse.onSuccess(pencilQueryService.getAcquiredPencils(member));
	}

	@Operation(summary = "얻은 연필 목록에서 읽음 처리를 진행한다.")
	@PatchMapping("/acquired/{acquiredPencilId}/read")
	public ApiResponse<Boolean> markAcquiredPencilAsRead(
		@PathVariable Long acquiredPencilId,
		@AuthenticationPrincipal Member member) {
		return ApiResponse.onSuccess(pencilCommandService.markAcquiredPencilAsRead(acquiredPencilId));
	}

	@Operation(summary = "구매한 연필 목록을 불러온다")
	@GetMapping("/purchased")
	public ApiResponse<List<PurchasedPencilResponse>> getPurchasedPencilHistory(
		@AuthenticationPrincipal Member member) {
		return ApiResponse.onSuccess(pencilQueryService.getPurchasedPencils(member));
	}

	@Operation(summary = "사용한 연필 목록을 불러온다")
	@GetMapping("/used")
	public ApiResponse<List<UsedPencilResponse>> getUsedPencilHistory(
		@AuthenticationPrincipal Member member) {
		return ApiResponse.onSuccess(pencilQueryService.getUsedPencils(member));
	}
}
