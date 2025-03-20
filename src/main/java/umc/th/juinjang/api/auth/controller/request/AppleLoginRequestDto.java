package umc.th.juinjang.api.auth.controller.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppleLoginRequestDto {

    @NotEmpty
    private String identityToken;
}
