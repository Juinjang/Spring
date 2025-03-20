package umc.th.juinjang.api.auth.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoSignUpRequestDto {

    @NotEmpty
    private String email;
    private String kakaoNickname;
    @NotEmpty
    private String nickname;
}
