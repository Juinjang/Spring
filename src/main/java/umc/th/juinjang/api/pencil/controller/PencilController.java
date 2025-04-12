package umc.th.juinjang.api.pencil.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.dto.ApiResponse;
import umc.th.juinjang.api.pencil.service.AcquiredPencilService;
import umc.th.juinjang.api.pencil.service.response.AcquiredPencilResponse;
import umc.th.juinjang.domain.member.model.Member;

@RestController
@RequestMapping("/api/v2/pencil")
@RequiredArgsConstructor
public class PencilController {

	private final AcquiredPencilService acquiredPencilService;

	@GetMapping("/acquired/history")
	public ApiResponse<List<AcquiredPencilResponse>> getAcquiredPencilHistory(@AuthenticationPrincipal Member member) {
		return ApiResponse.onSuccess(acquiredPencilService.getAcquiredPencils(member));
	}
}
