package umc.th.juinjang.api.pencil.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.apple.itunes.storekit.client.APIException;
import com.apple.itunes.storekit.model.JWSTransactionDecodedPayload;
import com.apple.itunes.storekit.verification.VerificationException;

import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.api.IntegrationTestSupport;
import umc.th.juinjang.api.apple.service.AppleService;
import umc.th.juinjang.api.apple.service.command.AppleTransactionVerifyCommand;
import umc.th.juinjang.api.pencil.controller.request.AppleIAPPurchaseRequest;
import umc.th.juinjang.api.pencil.service.response.AppleIAPPurchaseResponse;
import umc.th.juinjang.api.pencil.service.response.VerificationResult;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.member.repository.MemberRepository;
import umc.th.juinjang.domain.pencil.purchased.model.TransactionStatus;
import umc.th.juinjang.domain.pencil.purchased.repository.PurchasedPencilRepository;
import umc.th.juinjang.domain.pencilaccount.model.PencilAccount;
import umc.th.juinjang.domain.pencilaccount.repository.PencilAccountRepository;
import umc.th.juinjang.testutil.fixture.MemberFixture;

@Slf4j
public class PencilCommandServiceTest extends IntegrationTestSupport {

	@Value("${apple.iap.bundle-id}")
	private String bundleId;

	private final String transactionId = "transactionId";
	private final UUID appAccountToken = UUID.randomUUID();
	private final String productId = "productId";

	@Autowired
	private PencilCommandService pencilService;

	@Autowired
	private PurchasedPencilRepository purchasedPencilRepository;

	@Autowired
	private PencilAccountRepository pencilAccountRepository;

	@Autowired
	private MemberRepository memberRepository;

	@MockBean
	private AppleService appleService;

	@AfterEach
	void tearDown() {
		purchasedPencilRepository.deleteAllInBatch();
		pencilAccountRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
	}

	@DisplayName("애플 인앱 결제과 정상적으로 진행됩니다.")
	@Test
	void processAppleIAPPurchase_Success() throws APIException, VerificationException, IOException {
		// given
		Member member = MemberFixture.createDefaultMember();
		memberRepository.save(member);

		PencilAccount pencilAccount = PencilAccount.createPencilAccount(member);
		pencilAccountRepository.save(pencilAccount);

		AppleIAPPurchaseRequest request = createValidRequest();
		LocalDateTime now = LocalDateTime.now();

		JWSTransactionDecodedPayload payload = new JWSTransactionDecodedPayload();
		payload.setProductId(productId);
		payload.setAppAccountToken(appAccountToken);
		payload.setBundleId(bundleId);
		payload.setQuantity(20);
		payload.setTransactionId(transactionId);

		when(appleService.verifyAppleTransaction(any(AppleTransactionVerifyCommand.class)))
			.thenReturn(VerificationResult.ofSuccess(payload));

		// when
		AppleIAPPurchaseResponse response = pencilService.processAppleIAPPurchase(request, member,now);

		// then
		log.info("[RESPONSE - TRANSACTION_ID]: {}", response.getTransactionId());

		assertThat(response).isNotNull();
		assertThat(response.getTransactionId()).isEqualTo(transactionId);
		assertThat(response.getStatus()).isEqualTo(TransactionStatus.SUCCESS);
	}

	@DisplayName("애플 인앱 결제 중에 유효성 검증에서 실패한 경우 (트랜잭션 아이디가 불일치) 에 대한 테스트 진행")
	@Test
	void processAppleIAPPurchase_Validation_Fail() throws APIException, VerificationException, IOException {
		// given
		Member member = MemberFixture.createDefaultMember();
		memberRepository.save(member);

		PencilAccount pencilAccount = PencilAccount.createPencilAccount(member);
		pencilAccountRepository.save(pencilAccount);

		// when
		AppleIAPPurchaseRequest request = createValidRequest();
		LocalDateTime now = LocalDateTime.now();

		String payloadTransactionId = "invalidTransactionId";
		JWSTransactionDecodedPayload payload = new JWSTransactionDecodedPayload();
		payload.setProductId(productId);
		payload.setAppAccountToken(appAccountToken);
		payload.setBundleId(bundleId);
		payload.setQuantity(20);
		payload.setTransactionId(payloadTransactionId);

		// then
		when(appleService.verifyAppleTransaction(any(AppleTransactionVerifyCommand.class)))
			.thenReturn(VerificationResult.ofVerificationError());

		// when
		AppleIAPPurchaseResponse response = pencilService.processAppleIAPPurchase(request, member,now);

		// then
		assertThat(response).isNotNull();
		assertThat(response.getTransactionId()).isEqualTo(transactionId);
		assertThat(response.getStatus()).isEqualTo(TransactionStatus.VALIDATION_FAILED);
	}



	private AppleIAPPurchaseRequest createValidRequest() {
		return AppleIAPPurchaseRequest
			.of(transactionId, appAccountToken, 20L, 3000L, productId,10L);
	}
}


