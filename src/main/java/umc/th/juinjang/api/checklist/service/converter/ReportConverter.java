package umc.th.juinjang.api.checklist.service.converter;

import java.util.List;
import java.util.stream.Collectors;

import umc.th.juinjang.api.checklist.service.response.ChecklistAnswerAndReportResponseDTO;
import umc.th.juinjang.api.checklist.service.response.ChecklistAnswerResponseDTO;
import umc.th.juinjang.api.checklist.service.response.ReportResponseDTO;
import umc.th.juinjang.api.limjang.service.converter.LimjangDetailConverter;
import umc.th.juinjang.api.limjang.service.response.LimjangDetailResponseDTO;
import umc.th.juinjang.domain.checklist.model.ChecklistAnswer;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.report.model.Report;

public class ReportConverter {

	public static ReportResponseDTO.ReportV2DTO toReportV2Dto(Report report, Limjang limjang) {
		ReportResponseDTO.ReportV2DTO reportDTO = ReportResponseDTO.ReportV2DTO.builder()
			.reportId(report.getReportId())
			.indoorKeyWord(report.getIndoorKeyword())
			.publicSpaceKeyWord(report.getPublicSpaceKeyword())
			.locationConditionsWord(report.getLocationConditionsKeyword())
			.indoorRate(report.getIndoorRate())
			.publicSpaceRate(report.getPublicSpaceRate())
			.locationConditionsRate(report.getLocationConditionsRate())
			.totalRate(report.getTotalRate())
			.limjangId(limjang.getLimjangId())
			.build();
		return reportDTO;
	}

}
