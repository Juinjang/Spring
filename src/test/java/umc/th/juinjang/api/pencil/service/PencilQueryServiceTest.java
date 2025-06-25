package umc.th.juinjang.api.pencil.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.apple.itunes.storekit.model.ConsumptionRequest;
import com.apple.itunes.storekit.model.ConsumptionStatus;
import com.apple.itunes.storekit.model.LifetimeDollarsPurchased;
import com.apple.itunes.storekit.model.LifetimeDollarsRefunded;
import com.apple.itunes.storekit.model.Platform;
import com.apple.itunes.storekit.model.PlayTime;

import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.api.IntegrationTestSupport;
import umc.th.juinjang.api.pencil.service.response.AcquiredPencilResponse;
import umc.th.juinjang.api.pencil.service.response.PurchasedPencilResponse;
import umc.th.juinjang.api.pencil.service.response.UsedPencilResponse;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.member.repository.MemberRepository;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredPencil;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredType;
import umc.th.juinjang.domain.pencil.acquired.repository.AcquiredPencilRepository;
import umc.th.juinjang.domain.pencil.purchased.model.PurchasedPencil;
import umc.th.juinjang.domain.pencil.purchased.repository.PurchasedPencilRepository;
import umc.th.juinjang.domain.pencil.used.model.UsedPencil;
import umc.th.juinjang.domain.pencil.used.model.Usedtype;
import umc.th.juinjang.domain.pencil.used.repository.UsedPencilRepository;
import umc.th.juinjang.testutil.fixture.MemberFixture;

@Slf4j
class PencilQueryServiceTest extends IntegrationTestSupport {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private AcquiredPencilRepository acquiredPencilRepository;

	@Autowired
	private PurchasedPencilRepository purchasedPencilRepository;

	@Autowired
	private UsedPencilRepository usedPencilRepository;

	@Autowired
	private PencilQueryService pencilService;

	@AfterEach
	void tearDown() {
		purchasedPencilRepository.deleteAllInBatch();
		acquiredPencilRepository.deleteAllInBatch();
		usedPencilRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
	}

	@DisplayName("얻은 연필 목록이 없는 경우에는 빈 배열이 반환된다.")
	@Test
	void getEmptyAcquiredPencilsList() {
		// given
		Member member = MemberFixture.createDefaultMember();
		memberRepository.save(member);

		// when
		List<AcquiredPencilResponse> list = pencilService.getAcquiredPencils(member);

		// then
		assertThat(list).hasSize(0);
	}

	@DisplayName("얻은 연필 목록이 생성 시간(createdAt) 내림차순으로 정렬되어 반환된다.")
	@Test
	void getAcquiredPencilsOrderedByCreatedAtDesc() {
		// given
		Member member = MemberFixture.createDefaultMember();
		memberRepository.save(member);

		// 시간을 내림차순으로 생성 (최신 시간이 먼저 오도록)
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime time1 = now.minusHours(4);
		LocalDateTime time2 = now.minusHours(3);
		LocalDateTime time3 = now.minusHours(2);
		LocalDateTime time4 = now.minusHours(1);
		LocalDateTime time5 = now;

		// 명확한 순서로 데이터 생성 (시간 역순으로)
		AcquiredPencil pencil1 = createAcquiredPencilWithTime(time1, "노트 작성으로 연필 획득", 1L, 10L, false, AcquiredType.NOTE,
			member);
		AcquiredPencil pencil2 = createAcquiredPencilWithTime(time2, "연필팩 구매로 연필 추가", 2L, 20L, true, AcquiredType.SOLD,
			member);
		AcquiredPencil pencil3 = createAcquiredPencilWithTime(time3, "매물 판매로 연필 획득", 3L, 30L, false, AcquiredType.SOLD,
			member);
		AcquiredPencil pencil4 = createAcquiredPencilWithTime(time4, "다른 노트 작성으로 연필 획득", 4L, 15L, true,
			AcquiredType.NOTE, member);
		AcquiredPencil pencil5 = createAcquiredPencilWithTime(time5, "또 다른 매물 판매로 연필 획득", 5L, 25L, false,
			AcquiredType.SOLD, member);

		acquiredPencilRepository.saveAll(List.of(pencil1, pencil2, pencil3, pencil4, pencil5));

		// when
		List<AcquiredPencilResponse> foundPencils = pencilService.getAcquiredPencils(member);

		foundPencils.forEach(pencil ->
			log.info("[ACQUIRED PENCILS]: {}", pencil.getCreatedAt())
		);

		// then
		assertThat(foundPencils).hasSize(5)
			.extracting("content", "sharedNoteId", "acquiredQuantity")
			.containsExactly(
				// 최신 시간부터 나열 (내림차순)
				Tuple.tuple("또 다른 매물 판매로 연필 획득", 5L, 25L),
				Tuple.tuple("다른 노트 작성으로 연필 획득", 4L, 15L),
				Tuple.tuple("매물 판매로 연필 획득", 3L, 30L),
				Tuple.tuple("연필팩 구매로 연필 추가", 2L, 20L),
				Tuple.tuple("노트 작성으로 연필 획득", 1L, 10L)
			);
	}

	@DisplayName("구매한 연필 목록이 없는 경우에는 빈 배열이 반환된다.")
	@Test
	void getEmptyPurchasedPencilsList() {
		// given
		Member member = MemberFixture.createDefaultMember();
		memberRepository.save(member);

		// when
		List<PurchasedPencilResponse> list = pencilService.getPurchasedPencils(member);

		// then
		assertThat(list).hasSize(0);
	}

	@DisplayName("구매한 연필 목록이 생성 시간(createdAt) 내림차순으로 정렬되어 반환된다.")
	@Test
	void getPurchasedPencilsOrderedByCreatedAtDesc() {
		// given
		Member member = MemberFixture.createDefaultMember();
		memberRepository.save(member);

		// 시간을 내림차순으로 생성 (최신 시간이 먼저 오도록)
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime time1 = now.minusHours(4);
		LocalDateTime time2 = now.minusHours(3);
		LocalDateTime time3 = now.minusHours(2);
		LocalDateTime time4 = now.minusHours(1);
		LocalDateTime time5 = now;

		// 랜덤 UUID 생성을 위한 도우미
		UUID uuid1 = UUID.randomUUID();
		UUID uuid2 = UUID.randomUUID();
		UUID uuid3 = UUID.randomUUID();
		UUID uuid4 = UUID.randomUUID();
		UUID uuid5 = UUID.randomUUID();

		// 명확한 순서로 데이터 생성 (시간 역순으로)
		PurchasedPencil pencil1 = PurchasedPencil.successOf(member, "10개 연필팩", 10L, 1000L, 0,"transaction1", uuid1, time1);
		PurchasedPencil pencil2 = PurchasedPencil.successOf(member, "20개 연필팩", 20L, 2000L, 0,"transaction2", uuid2, time2);
		PurchasedPencil pencil3 = PurchasedPencil.successOf(member, "30개 연필팩", 30L, 3000L,0 ,"transaction3", uuid3, time3);
		PurchasedPencil pencil4 = PurchasedPencil.successOf(member, "15개 연필팩", 15L, 1500L, 0,"transaction4", uuid4, time4);
		PurchasedPencil pencil5 = PurchasedPencil.successOf(member, "25개 연필팩", 25L, 2500L, 0,"transaction5", uuid5, time5);

		purchasedPencilRepository.saveAll(List.of(pencil1, pencil2, pencil3, pencil4, pencil5));

		// when
		List<PurchasedPencilResponse> purchasedPencils = pencilService.getPurchasedPencils(member);

		purchasedPencils.forEach(pencil -> {
				log.info("[PENCILS]: CREATED_AT : {} ", pencil.getPurchasedAt());
			}
		);
		// then
		assertThat(purchasedPencils).hasSize(5)
			.extracting("title", "purchaseQuantity", "price")
			.containsExactly(
				Tuple.tuple("25개 연필팩", 25L, 2500L),
				Tuple.tuple("15개 연필팩", 15L, 1500L),
				Tuple.tuple("30개 연필팩", 30L, 3000L),
				Tuple.tuple("20개 연필팩", 20L, 2000L),
				Tuple.tuple("10개 연필팩", 10L, 1000L)
			);
	}

	@DisplayName("구매한 연필 목록에서 DeliveryStatus 가 에러인 경우에만 목록에 반환되지 않는다.")
	@Test
	void getPurchasedPencilsWithoutDeliveryStatus() {
		// given
		Member member = MemberFixture.createDefaultMember();
		memberRepository.save(member);

		LocalDateTime time = LocalDateTime.now();
		UUID uuid = UUID.randomUUID();

		PurchasedPencil pencil = PurchasedPencil.failedDueToServerError(member, "10개 연필팩", 10L, 1000L, 10,"transaction1", uuid, time);

		purchasedPencilRepository.saveAll(List.of(pencil));

		// when
		List<PurchasedPencilResponse> purchasedPencils = pencilService.getPurchasedPencils(member);

		assertThat(purchasedPencils).hasSize(0);
	}

	@DisplayName("구매한 연필 목록이 없는 경우에는 빈 배열이 반환된다.")
	@Test
	void getEmptyUsedPencilsList() {
		// given
		Member member = MemberFixture.createDefaultMember();
		memberRepository.save(member);

		// when
		List<UsedPencilResponse> list = pencilService.getUsedPencils(member);

		// then
		assertThat(list).hasSize(0);
	}

	@DisplayName("사용한 연필 목록이 생성 시간(createdAt) 내림차순으로 정렬되어 반환된다.")
	@Test
	void getUsedPencilsOrderedByCreatedAtDesc() {
		// given
		Member member = MemberFixture.createDefaultMember();
		memberRepository.save(member);

		UsedPencil usedPencil = UsedPencil.create(member, 1L, 10L, Usedtype.OWNED, "빌딩", 10L);
		usedPencilRepository.saveAll(List.of(usedPencil));

		// when
		List<UsedPencilResponse> usedPencils = pencilService.getUsedPencils(member);

		// then
		// TODO: 추후에, 시간이 OrderBy 가 정상적으로 되는 지 테스트가 필요
		assertThat(usedPencils).hasSize(1)
			.extracting("type", "buildingName", "sharedNoteId")
			.containsExactly(
				Tuple.tuple(Usedtype.OWNED, "빌딩", 1L)
			);
	}

	@DisplayName("ConsumptionRequest가 PurchasedPencil 데이터를 기반으로 올바르게 생성된다.")
	@Test
	void getConsumptionRequestFromPurchasedPencil() {
		// given
		Member member = MemberFixture.createDefaultMember();
		memberRepository.save(member);

		LocalDateTime now = LocalDateTime.now();
		String transactionId = "test-transaction-id";
		UUID appAccountToken = UUID.randomUUID();

		PurchasedPencil pencil = PurchasedPencil.successOf(
			member,
			"테스트 연필팩",
			20L,
			2000L,
			10,
			transactionId,
			appAccountToken,
			now
		);

		purchasedPencilRepository.save(pencil);

		// AcquiredPencil 데이터를 하나라도 만들어줘야 sampleContentProvided == true
		acquiredPencilRepository.save(
			AcquiredPencil.create(member, "노트 작성", 1L, 10L, false, AcquiredType.NOTE)
		);

		// when
		ConsumptionRequest request = pencilService.getConsumptionRequest(transactionId);

		// then
		assertThat(request).isNotNull();
		assertThat(request.getAppAccountToken()).isEqualTo(appAccountToken);
		assertThat(request.getDeliveryStatus().getValue()).isEqualTo(pencil.getDeliveryStatus().getAppleCode());
		assertThat(request.getPlayTime()).isEqualTo(PlayTime.FIVE_TO_SIXTY_MINUTES);
		assertThat(request.getLifetimeDollarsPurchased()).isEqualTo(LifetimeDollarsPurchased.ONE_CENT_TO_FORTY_NINE_DOLLARS_AND_NINETY_NINE_CENTS);
		assertThat(request.getLifetimeDollarsRefunded()).isEqualTo(LifetimeDollarsRefunded.ZERO_DOLLARS);
		assertThat(request.getCustomerConsented()).isTrue();
		assertThat(request.getSampleContentProvided()).isTrue();
		assertThat(request.getPlatform()).isEqualTo(Platform.APPLE);

		assertThat(request.getConsumptionStatus()).isEqualTo(ConsumptionStatus.NOT_CONSUMED);
	}


	private AcquiredPencil createAcquiredPencilWithTime(LocalDateTime createdAt, String content, Long sharedNoteId,
		Long acquiredQuantity, boolean isRead, AcquiredType type, Member member) {
		return AcquiredPencil.createWithDate(member, content, sharedNoteId, acquiredQuantity, isRead, type, createdAt);
	}

}
