package umc.th.juinjang.api.pencilAccount.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PencilQuantityGetResponse {
	private Long totalBalance;

	@Builder
	private PencilQuantityGetResponse(Long totalBalance) {
		this.totalBalance = totalBalance;
	}

	public static PencilQuantityGetResponse of(Long totalBalance) {
		return PencilQuantityGetResponse.builder().totalBalance(totalBalance).build();
	}
}
