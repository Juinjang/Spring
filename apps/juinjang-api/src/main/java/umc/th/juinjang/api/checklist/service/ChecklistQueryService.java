package umc.th.juinjang.api.checklist.service;

import umc.th.juinjang.api.checklist.service.response.ChecklistAnswerResponseDTO;
import umc.th.juinjang.api.checklist.service.response.ReportResponseDTO;

import java.util.List;

public interface ChecklistQueryService {
//    public List<ChecklistQuestionDTO.QuestionListDto> getChecklistQuestionListByVersion(int version);

    public List<ChecklistAnswerResponseDTO.AnswerDto> getChecklistAnswerListByLimjang(Long limjangId);
//    public List<ChecklistQuestionDTO.QuestionListDto> getChecklistByLimjang(Long limjangId);

    public ReportResponseDTO getReportByLimjangId(Long limjangId);


}
