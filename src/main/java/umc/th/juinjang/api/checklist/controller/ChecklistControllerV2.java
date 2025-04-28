package umc.th.juinjang.api.checklist.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.checklist.service.ChecklistQueryServiceV2;
import umc.th.juinjang.api.checklist.service.response.ChecklistAnswerResponseDTO;
import umc.th.juinjang.api.checklist.service.response.ReportResponseDTO;
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
		@PathVariable(name = "limjangId") Long noteId) {
		return ApiResponse.onSuccess(checklistQueryService.getChecklistAnswerListByLimjang(noteId));
	}

	@CrossOrigin
	@Operation(summary = "리포트 조회 V2")
	@GetMapping("/report/{noteId}")
	public ApiResponse<ReportResponseDTO.ReportV2DTO> getReport(
		@PathVariable(name = "noteId") Long noteId) {
		return ApiResponse.onSuccess(checklistQueryService.getReportByNoteId(noteId));
	}
}
