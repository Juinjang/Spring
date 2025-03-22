package umc.th.juinjang.external.openfeign.apple;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class AppleClientSecretGenerator {

    private static final String AUDIENCE = "https://appleid.apple.com";
    private final ApplePrivateKeyGenerator applePrivateKeyGenerator;

    @Value("${apple.key.id}")
    private String keyId;
    @Value("${apple.team-id}")
    private String teamId;
    @Value("${apple.client-id}")
    private String clientId;

    public String generateClientSecret() throws IOException {
        Date expirationDate = Date.from(LocalDateTime.now().plusDays(5)
                .atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setHeaderParam("alg", SignatureAlgorithm.ES256)
                .setHeaderParam("kid", keyId)
                .setIssuer(teamId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .setAudience(AUDIENCE)
                .setSubject(clientId)
                .signWith(applePrivateKeyGenerator.getPrivateKey(), SignatureAlgorithm.ES256)
                .compact();
    }
}
