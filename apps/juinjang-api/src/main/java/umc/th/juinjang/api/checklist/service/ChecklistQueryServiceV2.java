package umc.th.juinjang.api.checklist.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.api.checklist.service.response.ChecklistAnswerResponseDTO;
import umc.th.juinjang.api.checklist.service.response.ReportGetResponse;
import umc.th.juinjang.api.checklist.service.response.ReportWithLimjangResponseDTO;
import umc.th.juinjang.api.limjang.service.NoteFinder;
import umc.th.juinjang.api.limjang.service.response.LimjangDetailGetResponse;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.report.model.Report;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChecklistQueryServiceV2 {

	private final ChecklistAnswerFinder checklistAnswerFinder;
	private final ReportFinder reportFinder;
	private final NoteFinder noteFinder;

	public List<ChecklistAnswerResponseDTO.AnswerDto> getChecklistAnswerListByLimjang(Long noteId) {
		return checklistAnswerFinder.findByLimjangId(noteId);
	}

	public ReportWithLimjangResponseDTO getReportByNoteId(Long noteId) {
		Limjang note = noteFinder.getNoteByIdWithAddressAndNotePriceWhereDeletedIsFalse(noteId);
		Report report = reportFinder.findReportByNote(note);
		return new ReportWithLimjangResponseDTO(ReportGetResponse.of(report), LimjangDetailGetResponse.of(note));
	}
}
