package umc.th.juinjang.api.checklist.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.checklist.service.ChecklistQueryServiceV2;
import umc.th.juinjang.api.checklist.service.response.ChecklistAnswerResponseDTO;
import umc.th.juinjang.api.dto.ApiResponse;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
@Validated
public class ChecklistControllerV2 {

	private final ChecklistQueryServiceV2 checklistQueryService;

	@CrossOrigin
	@Operation(summary = "체크리스트 답변 조회")
	@GetMapping("/checklist/{limjangId}")
	public ApiResponse<List<ChecklistAnswerResponseDTO.AnswerDto>> getChecklistAnswer(
		@PathVariable(name = "limjangId") Long limjangId) {
		return ApiResponse.onSuccess(checklistQueryService.getChecklistAnswerListByLimjang(limjangId));
	}
}
