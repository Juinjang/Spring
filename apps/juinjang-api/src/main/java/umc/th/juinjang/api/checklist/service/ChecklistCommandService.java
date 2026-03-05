package umc.th.juinjang.api.checklist.service;

import umc.th.juinjang.api.checklist.service.response.ChecklistAnswerAndReportResponseDTO;
import umc.th.juinjang.api.checklist.controller.request.ChecklistAnswerRequestDTO;

import java.util.List;

public interface ChecklistCommandService {
    public ChecklistAnswerAndReportResponseDTO saveChecklistAnswerList(Long limjangId, List<ChecklistAnswerRequestDTO.AnswerDto> answerDtoList);
}
