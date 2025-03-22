package umc.th.juinjang.external.openfeign.apple;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import umc.th.juinjang.common.ExceptionHandler;
import umc.th.juinjang.common.code.status.ErrorStatus;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j

public class ApplePublicKeyGenerator {

    public PublicKey generatePublicKey(Map<String,String> tokenHeaders, ApplePublicKeyResponse applePublicKeys) {
        log.info("generate public Key");
        ApplePublicKeyResponse.Key publicKey = applePublicKeys.getMatchedKeyBy(tokenHeaders.get("kid"),tokenHeaders.get("alg"));
        return getPublicKey(publicKey);
    }

    private PublicKey getPublicKey(ApplePublicKeyResponse.Key publicKey) {

        log.info("get public Key!!!!!!");
        byte[] nBytes = Base64.getUrlDecoder().decode(publicKey.getN());
        byte[] eBytes = Base64.getUrlDecoder().decode(publicKey.getE());

        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(1, nBytes),
                new BigInteger(1, eBytes));

        log.info("get public Key!!!!!! publicKeySpec이후"+publicKeySpec);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(publicKey.getKty());
            log.info("get public Key!!!!!! return문 앞"+publicKeySpec);
            return keyFactory.generatePublic(publicKeySpec);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ExceptionHandler(ErrorStatus.PUBLICKEY_ERROR_IN_APPLE_LOGIN);
        }
    }
}
