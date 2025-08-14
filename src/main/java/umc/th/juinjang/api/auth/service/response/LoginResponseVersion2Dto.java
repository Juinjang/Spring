package umc.th.juinjang.api.auth.service.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponseVersion2Dto {

    private String accessToken;
    private String refreshToken;
    private String email;
    private String agreeVersion;

    @Builder
    public LoginResponseVersion2Dto(String accessToken, String refreshToken, String email, String agreeVersion) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.email = email;
        this.agreeVersion = agreeVersion;
    }
}