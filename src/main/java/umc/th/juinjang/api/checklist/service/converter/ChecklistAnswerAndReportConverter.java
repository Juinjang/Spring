package umc.th.juinjang.api.checklist.service.converter;

import umc.th.juinjang.api.limjang.service.converter.LimjangDetailConverter;
import umc.th.juinjang.api.checklist.service.response.ChecklistAnswerAndReportResponseDTO;
import umc.th.juinjang.api.checklist.service.response.ChecklistAnswerResponseDTO;
import umc.th.juinjang.api.checklist.service.response.ReportResponseDTO;
import umc.th.juinjang.api.limjang.service.response.LimjangDetailResponseDTO;
import umc.th.juinjang.domain.checklist.model.ChecklistAnswer;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.report.model.Report;

import java.util.List;
import java.util.stream.Collectors;

public class ChecklistAnswerAndReportConverter {

	public static ChecklistAnswerAndReportResponseDTO toDto(List<ChecklistAnswer> answerList, Report report,
		Limjang limjang) {
		List<ChecklistAnswerResponseDTO.AnswerDto> answerDtoList = answerList.stream()
			.map(answer -> new ChecklistAnswerResponseDTO.AnswerDto(
				answer.getAnswerId(),
				answer.getQuestionId().getQuestionId(),
				//answer.getQuestionId().getCategory(),
				answer.getLimjangId().getLimjangId(),
				answer.getAnswer(),
				answer.getQuestionId().getAnswerType()))
			.collect(Collectors.toList());

		ReportResponseDTO.ReportDTO reportDto = new ReportResponseDTO.ReportDTO(
			report.getReportId(),
			report.getIndoorKeyword(),
			report.getPublicSpaceKeyword(),
			report.getLocationConditionsKeyword(),
			report.getIndoorRate(),
			report.getPublicSpaceRate(),
			report.getLocationConditionsRate(),
			report.getTotalRate());

		LimjangDetailResponseDTO.DetailDto detailDto = LimjangDetailConverter.toDetail(limjang,
			limjang.getLimjangPrice());

		return new ChecklistAnswerAndReportResponseDTO(answerDtoList, new ReportResponseDTO(reportDto, detailDto));
	}

	public static ReportResponseDTO toReportDto(Report report, Limjang limjang) {
		ReportResponseDTO.ReportDTO reportDTO = ReportResponseDTO.ReportDTO.builder()
			.reportId(report.getReportId())
			.indoorKeyWord(report.getIndoorKeyword())
			.publicSpaceKeyWord(report.getPublicSpaceKeyword())
			.locationConditionsWord(report.getLocationConditionsKeyword())
			.indoorRate(report.getIndoorRate())
			.publicSpaceRate(report.getPublicSpaceRate())
			.locationConditionsRate(report.getLocationConditionsRate())
			.totalRate(report.getTotalRate())
			.build();
		LimjangDetailResponseDTO.DetailDto detailDto = LimjangDetailConverter.toDetail(limjang,
			limjang.getLimjangPrice());
		return new ReportResponseDTO(reportDTO, detailDto);
	}

}
