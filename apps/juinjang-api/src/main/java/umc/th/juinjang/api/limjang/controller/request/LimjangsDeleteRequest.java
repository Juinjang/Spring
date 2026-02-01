package umc.th.juinjang.api.limjang.controller.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public record LimjangsDeleteRequest(
	@NotEmpty List<Long> limjangIdList
) {
	public static LimjangsDeleteRequest of(List<Long> limjangIds) {
		return new LimjangsDeleteRequest(limjangIds);
	}
}