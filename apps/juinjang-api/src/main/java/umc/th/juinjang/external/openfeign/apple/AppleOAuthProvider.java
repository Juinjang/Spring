package umc.th.juinjang.external.openfeign.apple;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import umc.th.juinjang.common.exception.handler.MemberHandler;

import static umc.th.juinjang.common.code.status.ErrorStatus.FAILED_TO_LOAD_PRIVATE_KEY;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppleOAuthProvider {

    @Value("${apple.client-id}")
    private String clientId;
    private final String GRANTTYPE = "authorization_code";

    private final AppleClient appleClient;

    public String getAppleRefreshToken(final String code, final String clientSecret) {
        try {
            log.info("authrozation_code============="+code);
            AppleTokenResponse appleTokenResponse = appleClient.getAppleTokens(code, clientId, clientSecret, GRANTTYPE);
            log.info("Apple token response: {}", appleTokenResponse);
            return appleTokenResponse.refreshToken();
        } catch (Exception e) {
            log.error("Failed to get apple refresh token.");
            throw new MemberHandler(FAILED_TO_LOAD_PRIVATE_KEY);
        }
    }

    public void requestRevoke(final String refreshToken, final String clientSecret) {
        appleClient.revoke(clientSecret,refreshToken,clientId, "refresh_token");
        log.error("Failed to revoke apple refresh token.");
    }
}
