package umc.th.juinjang.api.checklist.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.ChecklistHandler;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.report.model.Report;
import umc.th.juinjang.domain.report.repository.ReportRepository;

@Component
@RequiredArgsConstructor
public class ReportFinder {
	private final ReportRepository reportRepository;

	public Report findReportByNote(Limjang limjang) {
		return reportRepository.findByLimjangId(limjang)
			.orElseThrow(() -> new ChecklistHandler(ErrorStatus.REPORT_NOTFOUND_ERROR));
	}
}
