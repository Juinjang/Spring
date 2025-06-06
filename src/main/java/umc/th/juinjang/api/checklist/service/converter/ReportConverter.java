package umc.th.juinjang.api.checklist.service.converter;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import umc.th.juinjang.api.checklist.service.response.ReportResponseDTO;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.report.model.Report;

public class ReportConverter {

	public static ReportResponseDTO.ReportV2DTO toReportV2Dto(Report report, Limjang limjang) {
		return ReportResponseDTO.ReportV2DTO.builder()
			.reportId(report.getReportId())
			.indoorKeyWord(report.getIndoorKeyword())
			.publicSpaceKeyWord(report.getPublicSpaceKeyword())
			.locationConditionsWord(report.getLocationConditionsKeyword())
			.indoorRate(report.getIndoorRate())
			.publicSpaceRate(report.getPublicSpaceRate())
			.locationConditionsRate(report.getLocationConditionsRate())
			.totalRate(report.getTotalRate())
			.limjangId(limjang.getLimjangId())
			.purposeType(limjang.getPurpose())
			.propertyType(limjang.getPropertyType())
			.priceType(limjang.getPriceType())
			.buildingName(limjang.getNickname())
			.images(
				limjang.getImageList().stream()
					.map(image -> image.getImageUrl())
					.limit(3)
					.collect(Collectors.toList())
			)
			.roadAddress(limjang.getAddressEntity().getRoadAddress())
			.addressDetail(limjang.getAddressEntity().getAddressDetail())
			.price(limjang.getLimjangPrice().getPrice(limjang.getPriceType(), limjang.getPurpose()))
			.monthlyRent(
				limjang.getPriceType() == LimjangPriceType.MONTHLY_RENT
					? limjang.getLimjangPrice().getMonthlyRent()
					: null
			)
			.updatedAt(limjang.getUpdatedAt().format(DateTimeFormatter.ofPattern("yy.MM.dd")))
			.floor(limjang.getFloor())
			.pyong(limjang.getPyong())
			.build();
	}

}
