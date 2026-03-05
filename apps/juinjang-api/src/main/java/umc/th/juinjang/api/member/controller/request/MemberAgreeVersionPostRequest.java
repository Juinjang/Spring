package umc.th.juinjang.api.member.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MemberAgreeVersionPostRequest(
    @Pattern(regexp = "\\d+\\.\\d+\\.\\d+", message = "x.x.x(x는 숫자) 형식이 아닙니다. 다시 확인해주세요.")
    @NotBlank
    String agreeVersion
) {
}
