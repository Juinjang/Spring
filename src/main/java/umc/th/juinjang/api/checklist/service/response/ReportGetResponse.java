package umc.th.juinjang.api.checklist.service.response;

import umc.th.juinjang.domain.report.model.Report;

public record ReportGetResponse(
	Long reportId,
	String indoorKeyWord,
	String publicSpaceKeyWord,
	String locationConditionsWord,
	Float indoorRate,
	Float publicSpaceRate,
	Float locationConditionsRate,
	Float totalRate
) {
	public static ReportGetResponse of(Report report) {
		return new ReportGetResponse(
			report.getReportId(),
			report.getIndoorKeyword(),
			report.getPublicSpaceKeyword(),
			report.getLocationConditionsKeyword(),
			report.getIndoorRate(),
			report.getPublicSpaceRate(),
			report.getLocationConditionsRate(),
			report.getTotalRate()
		);
	}
}