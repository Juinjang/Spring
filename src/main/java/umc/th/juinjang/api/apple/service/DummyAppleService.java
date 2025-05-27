package umc.th.juinjang.api.apple.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.apple.itunes.storekit.model.Environment;
import com.apple.itunes.storekit.model.JWSTransactionDecodedPayload;

import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.api.apple.service.command.AppleTransactionVerifyCommand;
import umc.th.juinjang.api.pencil.service.response.VerificationResult;

@Profile("local")
@Service
@Slf4j
public class DummyAppleService extends AppleService {

	@Override
	public VerificationResult verifyAppleTransaction(AppleTransactionVerifyCommand command) {
		log.info("🔧 [LOCAL] Apple Transaction dummy verification 성공 처리");

		JWSTransactionDecodedPayload dummyPayload = new JWSTransactionDecodedPayload()
			.transactionId(command.getTransactionId())
			.originalTransactionId("dummy-original-transaction-id")
			.bundleId("com.example.dummy")
			.productId(command.getProductId())
			.quantity(1)
			.purchaseDate(System.currentTimeMillis())
			.signedDate(System.currentTimeMillis())
			.environment(Environment.LOCAL_TESTING)
			.appAccountToken(command.getAppAccountToken());

		return VerificationResult.ofSuccess(dummyPayload);
	}
}
