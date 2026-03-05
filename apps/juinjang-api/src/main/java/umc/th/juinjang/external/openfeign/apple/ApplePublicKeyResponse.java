package umc.th.juinjang.external.openfeign.apple;

import lombok.Getter;
import umc.th.juinjang.common.ExceptionHandler;
import umc.th.juinjang.common.code.status.ErrorStatus;

import java.util.List;

@Getter
public class ApplePublicKeyResponse {
    private List<Key> keys;

    @Getter
    public static class Key {
        private String kty;
        private String kid;
        private String use;
        private String alg;
        private String n;
        private String e;
    }

    //받은 public key 중 kid alg 같은거 찾기
    // Identity Token 헤더에 있는것과비교
    public Key getMatchedKeyBy(String kid, String alg) {
        return keys.stream()
                .filter(key -> key.getKid().equals(kid) && key.getAlg().equals(alg))
                .findAny()
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.INVALID_APPLE_ID_TOKEN_INFO));
    }

}
