package umc.th.juinjang.api.pencil.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apple.itunes.storekit.model.JWSTransactionDecodedPayload;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.api.apple.service.AppleService;
import umc.th.juinjang.api.pencil.controller.request.AppleIAPPurchaseRequest;
import umc.th.juinjang.api.pencil.service.response.AppleIAPPurchaseResponse;
import umc.th.juinjang.api.pencilAccount.service.PencilAccountFinder;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredPencil;
import umc.th.juinjang.domain.pencil.purchased.model.PurchasedPencil;
import umc.th.juinjang.domain.pencilaccount.model.PencilAccount;

@Slf4j
@Service
@RequiredArgsConstructor
public class PencilCommandService {

	private final AppleService appleService;

	private final PurchasedPencilUpdater purchasedPencilUpdater;
	private final AcquiredPencilFinder acquiredPencilFinder;
	private final PencilAccountFinder pencilAccountFinder;

	@Value("${apple.iap.bundle-id}")
	String appleBundleId;

	@Transactional
	public Boolean markAcquiredPencilAsRead(Long acquiredPencilId) {
		AcquiredPencil acquiredPencil = acquiredPencilFinder.findById(acquiredPencilId);

		if (acquiredPencil == null) {
			throw new EntityNotFoundException("AcquiredPencil not found with id: " + acquiredPencilId);
		}

		acquiredPencil.updateIsReadAsTrue();
		return true;
	}

	@Transactional
	public AppleIAPPurchaseResponse processAppleIAPPurchase(AppleIAPPurchaseRequest request, Member member,
		LocalDateTime now) {
		String transactionId = request.getTransactionId();
		Long pencilAmount = request.getPencilQuantity();

		// 트랜잭션이 중복되는 지 체크가 필요한 가?
		try{
			// 1. 애플 서버로부터 트랜잭션 정보 가져오기
			JWSTransactionDecodedPayload decodedPayload = appleService.getTransactionInfo(transactionId);

			// 2. 서버와의 검증 수행
			if (!validateTransaction(decodedPayload,request)){
				// PurchasePencil에 결과 저장
				return null;
			}

			PencilAccount pencilAccount = pencilAccountFinder.findByMemberWithLock(member);
			pencilAccount.increasePurchasedBalance(pencilAmount);

			String title = "연필 10개 구매";

			PurchasedPencil purchasedPencil = PurchasedPencil.createSuccessPurchase(
				member, title, 10L, pencilAmount, transactionId, request.getAppAccountToken(),now);

			purchasedPencilUpdater.save(purchasedPencil);

			log.info("Apple IAP Purchase Success. Transaction ID: {}, Total Balance Amount: {} , Total Purchase Amount: {}",
				transactionId, pencilAccount.getTotalBalance(), pencilAccount.getTotalPurchaseAmount());
			return AppleIAPPurchaseResponse.of(transactionId, pencilAccount.getTotalBalance());
		}catch (Exception e){
			return null;
		}
	}

	private boolean validateTransaction(JWSTransactionDecodedPayload decodedPayload, AppleIAPPurchaseRequest request) {
		// 트랜잭션 아이디가 정상적으로 일치하는 지 여부
		if (!decodedPayload.getTransactionId().equals(request.getTransactionId())) {
			log.warn("트랜잭션 아이디 불일치. 애플 PAYLOAD : {}, REQUEST 요청 : {}",decodedPayload.getTransactionId(), request.getTransactionId());
			return false;
		}

		// 1. 환불/취소 여부 확인
		if (decodedPayload.getRevocationDate() != null || decodedPayload.getRevocationReason() != null) {
			log.warn("트랜잭션이 취소되었습니다. 트랜잭션 ID: {}, 취소 이유: {}",
				decodedPayload.getTransactionId(), decodedPayload.getRevocationReason());
			return false;
		}

		// 2. 번들 ID가 앱의 번들 ID와 일치하는지 검증
		if (!appleBundleId.equals(decodedPayload.getBundleId())) {
			log.warn("번들 ID 불일치. 예상: {}, 실제: {}",
				appleBundleId, decodedPayload.getBundleId());
			return false;
		}

		// 3. 상품 ID가 요청한 상품과 일치하는지 검증
		if (!request.getProductId().equals(decodedPayload.getProductId())) {
			log.warn("상품 ID 불일치. 요청: {}, 응답: {}",
				request.getProductId(), decodedPayload.getProductId());
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
		if (request.getAppAccountToken() != null && decodedPayload.getAppAccountToken() != null &&
			!request.getAppAccountToken().equals(decodedPayload.getAppAccountToken())) {
			log.warn("앱 계정 토큰 불일치. 요청: {}, 응답: {}",
				request.getAppAccountToken(), decodedPayload.getAppAccountToken());
			return false;
		}

		// 7. 모든 검증이 완료되었으므로 true 반환
		log.info("Apple IAP Purchase Validation Success. Transaction ID: {}", decodedPayload.getTransactionId());
		return true;
	}
}
