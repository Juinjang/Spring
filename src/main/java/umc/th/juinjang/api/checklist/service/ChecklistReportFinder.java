package umc.th.juinjang.api.checklist.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import umc.th.juinjang.api.checklist.service.converter.ChecklistAnswerAndReportConverter;
import umc.th.juinjang.api.checklist.service.response.ReportResponseDTO;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.ChecklistHandler;
import umc.th.juinjang.common.exception.handler.LimjangHandler;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.repository.LimjangRepository;
import umc.th.juinjang.domain.report.model.Report;
import umc.th.juinjang.domain.report.repository.ReportRepository;

@Component
@RequiredArgsConstructor
public class ChecklistReportFinder {

	private final LimjangRepository limjangRepository;
	private final ReportRepository reportRepository;

	public ReportResponseDTO.ReportV2DTO findReportByNoteId(Long noteId) {
		Limjang limjang = limjangRepository.findByLimjangIdAndDeletedIsFalse(noteId)
			.orElseThrow(() -> new LimjangHandler(ErrorStatus.LIMJANG_NOTFOUND_ERROR));

		Report report = reportRepository.findByLimjangId(limjang)
			.orElseThrow(() -> new ChecklistHandler(ErrorStatus.REPORT_NOTFOUND_ERROR));

		return ChecklistAnswerAndReportConverter.toReportV2Dto(report, limjang);
	}
}
