package umc.th.juinjang.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import umc.th.juinjang.apiPayload.ApiResponse;
import umc.th.juinjang.model.dto.checklist.ChecklistAnswerAndReportResponseDTO;
import umc.th.juinjang.model.dto.checklist.ChecklistAnswerRequestDTO;
import umc.th.juinjang.model.dto.checklist.ChecklistAnswerResponseDTO;
import umc.th.juinjang.model.dto.checklist.ReportResponseDTO;
import umc.th.juinjang.service.checklist.ChecklistCommandService;
import umc.th.juinjang.service.checklist.ChecklistQueryService;

import java.util.List;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
@Validated
public class ChecklistControllerV2 {

}
