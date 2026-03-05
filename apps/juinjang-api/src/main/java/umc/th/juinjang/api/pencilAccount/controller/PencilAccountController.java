package umc.th.juinjang.api.pencilAccount.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.dto.ApiResponse;
import umc.th.juinjang.api.pencilAccount.service.PencilAccountService;
import umc.th.juinjang.api.pencilAccount.service.response.PencilQuantityGetResponse;
import umc.th.juinjang.domain.member.model.Member;

@RestController
@RequestMapping("/api/v2/pencil-account")
@RequiredArgsConstructor
public class PencilAccountController {

	private final PencilAccountService pencilAccountService;

	@GetMapping("/balance")
	public ApiResponse<PencilQuantityGetResponse> getTotalPencilAmountByMember(
		@AuthenticationPrincipal Member member
	) {
		return ApiResponse.onSuccess(
			PencilQuantityGetResponse.of(pencilAccountService.getTotalPencilAmountByMember(member)));
	}

}
