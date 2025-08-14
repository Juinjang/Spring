package umc.th.juinjang.api.auth.controller.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppleSignUpRequestVersion2Dto {

    @NotEmpty
    private String identityToken;
    @NotEmpty
    private String nickname;

    @NotEmpty
    private String agreeVersion;

}
