package umc.th.juinjang.api.auth.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class AppleTokenRequest {
    private String client_id;
    private String client_secret;
    private String code; //authorization code
    private String grant_type;
}
