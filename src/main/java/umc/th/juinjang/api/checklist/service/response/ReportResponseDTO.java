package umc.th.juinjang.api.checklist.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import umc.th.juinjang.api.limjang.service.response.LimjangDetailResponseDTO;

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
}
