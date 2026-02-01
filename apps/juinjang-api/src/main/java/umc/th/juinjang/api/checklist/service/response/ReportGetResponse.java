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

	public static ReportGetResponse mock() {
		return new ReportGetResponse(
			1L,
			"상당히 쾌적한 실내",
			"훌륭한 공용 공간",
			"좋은 편인 입지 조건",
			4.6f,
			4.3f,
			4.7f,
			4.5f
		);
	}
}