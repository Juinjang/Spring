package umc.th.juinjang.api.apple.service;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.apple.itunes.storekit.client.APIException;
import com.apple.itunes.storekit.client.AppStoreServerAPIClient;
import com.apple.itunes.storekit.model.Environment;
import com.apple.itunes.storekit.model.JWSTransactionDecodedPayload;
import com.apple.itunes.storekit.model.TransactionInfoResponse;
import com.apple.itunes.storekit.verification.SignedDataVerifier;
import com.apple.itunes.storekit.verification.VerificationException;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AppleService {

	@Value("${apple.iap.bundle-id}")
	private String bundleId;

	@Value("${apple.iap.key-id}")
	private String keyId;

	@Value("${apple.iap.issuer-id}")
	private String issuerId;

	@Value("${apple.iap.apple-id}")
	private String appleIdStr;

	@Value("${apple.iap.environment}")
	private String environmentString; // SANDBOX , PRODUCTION

	@Value("${apple.iap.certificate-names}")
	private String certificateConfigs;

	@Value("${apple.iap.private-key-path}")
	private String privateKeyPath;

	private  SignedDataVerifier signedDataVerifier;
	private AppStoreServerAPIClient appStoreServerAPIClient;

	@PostConstruct
	public void init() {
		Set<InputStream> rootCertificates = loadRootCertificates();

		Environment environment = Environment.fromValue(environmentString);
		Long appleId = Long.valueOf(appleIdStr);

		this.signedDataVerifier = new SignedDataVerifier(
			rootCertificates,
			bundleId,
			appleId,
			environment,
			true
		);


		String signingKey = loadSigningKey();

		this.appStoreServerAPIClient = new AppStoreServerAPIClient(
			signingKey,
			issuerId,
			keyId,
			bundleId,
			environment
		);

	}

	public JWSTransactionDecodedPayload getTransactionInfo(String transactionId) throws APIException, IOException, VerificationException {
		TransactionInfoResponse transactionInfo = appStoreServerAPIClient.getTransactionInfo(transactionId);
		return signedDataVerifier.verifyAndDecodeTransaction(transactionInfo.getSignedTransactionInfo());
	}


	private Set<InputStream> loadRootCertificates() {
		try {
			Set<InputStream> certificates = new HashSet<>();
			String[] certConfigs = certificateConfigs.split(",");

			for (String name : certConfigs) {
				String certPath = "certs/" + name.trim();
				ClassPathResource resource = new ClassPathResource(certPath);

				if (resource.exists()) {
					log.info("Loading certificate: {}", certPath);
					certificates.add(resource.getInputStream());
				} else {
					log.warn("Certificate not found: {}", certPath);
				}
			}

			if (certificates.isEmpty()) {
				log.error("No certificates were loaded");
				throw new RuntimeException("Failed to load any certificates");
			}

			return certificates;
		} catch (Exception e) {
			log.error("Error loading root certificates: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to load root certificates", e);
		}
	}

	private String loadSigningKey() {
		try {
			log.info("Loading signing key from: {}", privateKeyPath);

			ClassPathResource resource = new ClassPathResource(privateKeyPath);
			String privateKeyContent;

			try (InputStream inputStream = resource.getInputStream()) {
				privateKeyContent = new String(inputStream.readAllBytes());
			}

			privateKeyContent = privateKeyContent
				.replace("-----BEGIN PRIVATE KEY-----", "")
				.replace("-----END PRIVATE KEY-----", "")
				.replaceAll("\\s", "");

			log.info("Signing key loaded successfully");
			return privateKeyContent;

		} catch (Exception e) {
			log.error("Failed to load signing key: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to load signing key", e);
		}
	}
}
