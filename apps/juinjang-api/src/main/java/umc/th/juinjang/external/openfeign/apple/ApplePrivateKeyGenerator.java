package umc.th.juinjang.external.openfeign.apple;

import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

@Component
public class ApplePrivateKeyGenerator {

    public PrivateKey getPrivateKey() throws IOException {
        // .p8 파일의 경로를 가져옴.
        ClassPathResource resource = new ClassPathResource("AUTHKEY_JUINJAG.p8");

        // 파일의 내용을 String으로 읽어옴.
        String privateKeyContent = new String(Files.readAllBytes(Paths.get(resource.getURI())));

        // PEM 파일의 헤더와 푸터를 제거하고 Base64 인코딩된 문자열을 추출.
        String privateKeyPEM = privateKeyContent
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        // Base64로 인코딩된 문자열을 디코딩.
        byte[] encoded = Base64.decodeBase64(privateKeyPEM);

        // PKCS8EncodedKeySpec을 생성.
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);

        try {
            // KeyFactory를 사용하여 PrivateKey 객체를 생성
            KeyFactory keyFactory = KeyFactory.getInstance("EC"); // 키 타입에 따라 "RSA" 또는 "EC"를 사용.
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new IOException("Failed to convert private key.", e);
        }
    }
}
