package umc.th.juinjang.api.checklist.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import umc.th.juinjang.api.limjang.service.response.LimjangDetailGetResponse;

@Getter
@Setter
@AllArgsConstructor
public class ReportWithLimjangResponseDTO {
	private ReportGetResponse reportDTO;
	private LimjangDetailGetResponse limjangDto;
}