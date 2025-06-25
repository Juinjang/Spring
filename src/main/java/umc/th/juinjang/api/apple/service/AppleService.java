package umc.th.juinjang.api.apple.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.apple.itunes.storekit.client.APIException;
import com.apple.itunes.storekit.client.AppStoreServerAPIClient;
import com.apple.itunes.storekit.model.ConsumptionRequest;
import com.apple.itunes.storekit.model.Data;
import com.apple.itunes.storekit.model.Environment;
import com.apple.itunes.storekit.model.JWSTransactionDecodedPayload;
import com.apple.itunes.storekit.model.ResponseBodyV2;
import com.apple.itunes.storekit.model.ResponseBodyV2DecodedPayload;
import com.apple.itunes.storekit.model.TransactionInfoResponse;
import com.apple.itunes.storekit.verification.SignedDataVerifier;
import com.apple.itunes.storekit.verification.VerificationException;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.api.apple.service.command.AppleTransactionVerifyCommand;
import umc.th.juinjang.api.pencil.service.PencilQueryService;
import umc.th.juinjang.api.pencil.service.response.VerificationResult;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.AppleHandler;

@Slf4j
@Service
@Profile("!local")
@RequiredArgsConstructor
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

	private SignedDataVerifier signedDataVerifier;
	private AppStoreServerAPIClient appStoreServerAPIClient;
	private PencilQueryService pencilQueryService;

	@PostConstruct
	public void init() {

		log.info("Apple IAP 초기화 시작");
		log.info("Bundle ID: {}", bundleId);
		log.info("Key ID: {}", keyId);
		log.info("Issuer ID: {}", issuerId);
		log.info("Environment: {}", environmentString);
		log.info("Private Key Path: {}", privateKeyPath);

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
			keyId,
			issuerId,
			bundleId,
			environment
		);

	}

	@Retryable(
		maxAttempts = 3,
		backoff = @Backoff(delay = 1000),
		retryFor = {APIException.class, IOException.class, VerificationException.class})
	public JWSTransactionDecodedPayload getTransactionInfo(String transactionId) throws
		APIException,
		IOException,
		VerificationException {
		log.info("Executing GetTransactionInfo for TRANSACTION_ID: {} - Thread: {}",
			transactionId, Thread.currentThread().getName());

		TransactionInfoResponse transactionInfo = appStoreServerAPIClient.getTransactionInfo(transactionId);
		return signedDataVerifier.verifyAndDecodeTransaction(transactionInfo.getSignedTransactionInfo());
	}

	public VerificationResult verifyAppleTransaction(AppleTransactionVerifyCommand command) {
		try {
			JWSTransactionDecodedPayload payload = getTransactionInfo(command.getTransactionId());

			if (!validateTransaction(payload, command)) {
				return VerificationResult.ofVerificationError();
			}

			return VerificationResult.ofSuccess(payload);

		} catch (IOException | APIException e) {
			log.warn("❌ Apple transaction verification error. transactionId: {}", command.getTransactionId(), e);
			return VerificationResult.ofServerError();
		} catch (VerificationException e) {
			log.warn("❌ Apple transaction verification error. transactionId: {}", command.getTransactionId(), e);
			return VerificationResult.ofVerificationError();
		}
	}

	public void sendConsumptionData(String transactionId, ConsumptionRequest request) {
		try {
			appStoreServerAPIClient.sendConsumptionData(transactionId, request);
		} catch (IOException | APIException e) {
			throw new AppleHandler(ErrorStatus.APPLE_VERIFICATION_ERROR);
		}

	}

	public ResponseBodyV2DecodedPayload getNotificationPayload(ResponseBodyV2 responseBody) {
		try {
			return signedDataVerifier.verifyAndDecodeNotification(
				responseBody.getSignedPayload());
		} catch (VerificationException e) {
			throw new AppleHandler(ErrorStatus.APPLE_VERIFICATION_ERROR);
		}
	}

	public JWSTransactionDecodedPayload getSignedTransactionPayload(
		Data data
	) {
		try {
			return signedDataVerifier.verifyAndDecodeTransaction(
				data.getSignedTransactionInfo());
		} catch (VerificationException e) {
			throw new AppleHandler(ErrorStatus.APPLE_VERIFICATION_ERROR);
		}
	}

	private boolean validateTransaction(JWSTransactionDecodedPayload decodedPayload,
		AppleTransactionVerifyCommand command) {
		// 트랜잭션 아이디가 정상적으로 일치하는 지 여부
		if (!decodedPayload.getTransactionId().equals(command.getTransactionId())) {
			log.warn("트랜잭션 아이디 불일치. 애플 PAYLOAD : {}, REQUEST 요청 : {}", decodedPayload.getTransactionId(),
				command.getTransactionId());
			return false;
		}

		// 1. 환불/취소 여부 확인
		if (decodedPayload.getRevocationDate() != null || decodedPayload.getRevocationReason() != null) {
			log.warn("트랜잭션이 취소되었습니다. 트랜잭션 ID: {}, 취소 이유: {}",
				decodedPayload.getTransactionId(), decodedPayload.getRevocationReason());
			return false;
		}

		// 2. 번들 ID가 앱의 번들 ID와 일치하는지 검증
		if (!bundleId.equals(decodedPayload.getBundleId())) {
			log.warn("번들 ID 불일치. 예상: {}, 실제: {}",
				bundleId, decodedPayload.getBundleId());
			return false;
		}

		// 3. 상품 ID가 요청한 상품과 일치하는지 검증
		if (!command.getProductId().equals(decodedPayload.getProductId())) {
			log.warn("상품 ID 불일치. 요청: {}, 응답: {}",
				command.getProductId(), decodedPayload.getProductId());
			return false;
		}

		// 4. 환경 확인 - 프로덕션에서는 프로덕션, 개발에서는 샌드박스인지 확인
		// boolean isProduction = !"Sandbox".equalsIgnoreCase(decodedPayload.getEnvironment());
		// if (isProduction) {
		// 	log.warn("환경 불일치. 프로덕션 여부: {}, 프로덕션이어야 함: {}",
		// 		isProduction, shouldBeProduction);
		// 	return false;
		// }

		// 5. 수량 검증
		if (decodedPayload.getQuantity() <= 0) {
			log.warn("유효하지 않은 수량: {}", decodedPayload.getQuantity());
			return false;
		}

		// 6. 앱 계정 토큰이 제공된 경우 일치하는지 확인
		if (command.getAppAccountToken() != null && decodedPayload.getAppAccountToken() != null &&
			!command.getAppAccountToken().equals(decodedPayload.getAppAccountToken())) {
			log.warn("앱 계정 토큰 불일치. 요청: {}, 응답: {}",
				command.getAppAccountToken(), decodedPayload.getAppAccountToken());
			return false;
		}

		// 7. 모든 검증이 완료되었으므로 true 반환
		log.info("Apple IAP Purchase Validation Success. Transaction ID: {}", decodedPayload.getTransactionId());
		return true;
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

			log.info("Signing key loaded successfully");
			return privateKeyContent;

		} catch (Exception e) {
			log.error("Failed to load signing key: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to load signing key", e);
		}
	}

}
