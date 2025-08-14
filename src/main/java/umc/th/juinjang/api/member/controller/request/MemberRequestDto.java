package umc.th.juinjang.api.member.controller.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRequestDto {
	private String nickname;

	@Builder
	public MemberRequestDto(String nickname) {
		this.nickname = nickname;
	}
}
