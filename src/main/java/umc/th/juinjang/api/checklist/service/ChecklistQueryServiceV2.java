package umc.th.juinjang.api.checklist.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.api.checklist.service.converter.ChecklistAnswerAndReportConverter;
import umc.th.juinjang.api.checklist.service.response.ChecklistAnswerResponseDTO;
import umc.th.juinjang.api.checklist.service.response.ReportResponseDTO;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.ChecklistHandler;
import umc.th.juinjang.common.exception.handler.LimjangHandler;
import umc.th.juinjang.domain.checklist.model.ChecklistAnswer;
import umc.th.juinjang.domain.checklist.repository.ChecklistAnswerRepository;
import umc.th.juinjang.domain.checklist.repository.ChecklistQuestionRepository;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.repository.LimjangRepository;
import umc.th.juinjang.domain.report.model.Report;
import umc.th.juinjang.domain.report.repository.ReportRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChecklistQueryServiceV2 {

	private final ChecklistAnswerFinder checklistAnswerFinder;

	public List<ChecklistAnswerResponseDTO.AnswerDto> getChecklistAnswerListByLimjang(Long noteId) {
		return checklistAnswerFinder.findByLimjangId(noteId);
	}

}
