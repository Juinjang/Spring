package umc.th.juinjang.api.checklist.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.checklist.controller.request.ChecklistAnswerRequestDTO;
import umc.th.juinjang.api.checklist.service.ChecklistCommandServiceV2;
import umc.th.juinjang.api.checklist.service.ChecklistQueryServiceV2;
import umc.th.juinjang.api.checklist.service.response.ChecklistAnswerAndReportResponseDTO;
import umc.th.juinjang.api.checklist.service.response.ChecklistAnswerResponseDTO;
import umc.th.juinjang.api.checklist.service.response.ReportWithLimjangResponseDTO;
import umc.th.juinjang.api.dto.ApiResponse;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
@Validated
public class ChecklistControllerV2 {

	private final ChecklistQueryServiceV2 checklistQueryService;
	private final ChecklistCommandServiceV2 checklistCommandService;

	@CrossOrigin
	@Operation(summary = "체크리스트 답변 조회")
	@GetMapping("/checklist/{limjangId}")
	public ApiResponse<List<ChecklistAnswerResponseDTO.AnswerDto>> getChecklistAnswer(
		@PathVariable(name = "limjangId") Long noteId) {
		return ApiResponse.onSuccess(checklistQueryService.getChecklistAnswerListByLimjang(noteId));
	}

	@CrossOrigin
	@Operation(summary = "리포트 조회 V2")
	@GetMapping("/report/{noteId}")
	public ApiResponse<ReportWithLimjangResponseDTO> getReport(
		@PathVariable(name = "noteId") Long noteId) {
		return ApiResponse.onSuccess(checklistQueryService.getReportByNoteId(noteId));
	}

	@CrossOrigin
	@Operation(summary = "체크리스트 답변 생성/수정")
	@PostMapping("/checklist/{limjangId}")
	public ApiResponse<ChecklistAnswerAndReportResponseDTO> postChecklistAnswer(
		@PathVariable(name = "limjangId") Long limjangId,
		@RequestBody List<ChecklistAnswerRequestDTO.AnswerDto> answerDtos) {
		return ApiResponse.onSuccess(checklistCommandService.saveChecklistAnswerList(limjangId, answerDtos));
	}

}
