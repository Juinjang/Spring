package umc.th.juinjang.api.pencil.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.dto.ApiResponse;
import umc.th.juinjang.api.pencil.service.PencilService;
import umc.th.juinjang.api.pencil.service.response.AcquiredPencilResponse;
import umc.th.juinjang.api.pencil.service.response.PurchasedPencilsResponse;
import umc.th.juinjang.domain.member.model.Member;

@RestController
@RequestMapping("/api/v2/pencil")
@RequiredArgsConstructor
public class PencilController {

	private final PencilService pencilService;

	@Operation(summary = "얻은 연필 목록을 불러온다.")
	@GetMapping("/acquired")
	public ApiResponse<List<AcquiredPencilResponse>> getAcquiredPencilHistory(@AuthenticationPrincipal Member member) {
		return ApiResponse.onSuccess(pencilService.getAcquiredPencils(member));
	}

	@Operation(summary = "구매한 연필 목록을 불러온다")
	@GetMapping("/purchased")
	public ApiResponse<List<PurchasedPencilsResponse>> getPurchasedPencilHistory(
		@AuthenticationPrincipal Member member) {
		return ApiResponse.onSuccess(pencilService.getPurchasedPencils(member));
	}
}
