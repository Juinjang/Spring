package umc.th.juinjang.api.checklist.service.response;

import java.util.List;

import lombok.*;
import umc.th.juinjang.api.limjang.service.response.LimjangDetailResponseDTO;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.limjang.model.LimjangPurpose;

@AllArgsConstructor
@Getter
@Setter
public class ReportResponseDTO {
	private ReportDTO reportDTO;
	private LimjangDetailResponseDTO.DetailDto limjangDto;

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class ReportDTO {
		private Long reportId;
		private String indoorKeyWord;
		private String publicSpaceKeyWord;
		private String locationConditionsWord;
		private Float indoorRate;
		private Float publicSpaceRate;
		private Float locationConditionsRate;
		private Float totalRate;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class ReportV2DTO {
		private Long reportId;
		private String indoorKeyWord;
		private String publicSpaceKeyWord;
		private String locationConditionsWord;
		private Float indoorRate;
		private Float publicSpaceRate;
		private Float locationConditionsRate;
		private Float totalRate;
		private Long limjangId;
		LimjangPurpose purposeType;
		LimjangPropertyType propertyType;
		LimjangPriceType priceType;
		String buildingName;
		List<String> images;
		String roadAddress;
		String addressDetail;
		String price;
		String monthlyRent;
		String updatedAt;
		String floor;
		Integer pyong;
	}
}
