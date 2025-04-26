package umc.th.juinjang.api.pencil.service;

import static org.assertj.core.api.Assertions.*;
import static umc.th.juinjang.domain.pencil.purchased.model.PurchasedPencil.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.api.IntegrationTestSupport;
import umc.th.juinjang.api.pencil.service.response.AcquiredPencilResponse;
import umc.th.juinjang.api.pencil.service.response.PurchasedPencilsResponse;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.member.repository.MemberRepository;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredPencil;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredType;
import umc.th.juinjang.domain.pencil.acquired.repository.AcquiredPencilRepository;
import umc.th.juinjang.domain.pencil.purchased.model.PurchasedPencil;
import umc.th.juinjang.domain.pencil.purchased.repository.PurchasedPencilRepository;
import umc.th.juinjang.testutil.fixture.MemberFixture;

@Slf4j
class PencilServiceTest extends IntegrationTestSupport {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private AcquiredPencilRepository acquiredPencilRepository;

	@Autowired
	private PurchasedPencilRepository purchasedPencilRepository;

	@Autowired
	private PencilService pencilService;

	@AfterEach
	void tearDown() {
		purchasedPencilRepository.deleteAllInBatch();
		acquiredPencilRepository.deleteAllInBatch();
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
		List<PurchasedPencilsResponse> list = pencilService.getPurchasedPencils(member);

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
		PurchasedPencil pencil1 = createSuccessPurchase(member, "10개 연필팩", 10L, 1000L, "transaction1", uuid1, time1);
		PurchasedPencil pencil2 = createSuccessPurchase(member, "20개 연필팩", 20L, 2000L, "transaction2", uuid2, time2);
		PurchasedPencil pencil3 = createSuccessPurchase(member, "30개 연필팩", 30L, 3000L, "transaction3", uuid3, time3);
		PurchasedPencil pencil4 = createSuccessPurchase(member, "15개 연필팩", 15L, 1500L, "transaction4", uuid4, time4);
		PurchasedPencil pencil5 = createSuccessPurchase(member, "25개 연필팩", 25L, 2500L, "transaction5", uuid5, time5);

		purchasedPencilRepository.saveAll(List.of(pencil1, pencil2, pencil3, pencil4, pencil5));

		// when
		List<PurchasedPencilsResponse> purchasedPencils = pencilService.getPurchasedPencils(member);

		purchasedPencils.forEach(pencil -> {
				log.info("[PENCILS]: CREATED_AT : {} ", pencil.getCreatedAt());
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

		PurchasedPencil pencil = createServerErrorPurchase(member, "10개 연필팩", 10L, 1000L, "transaction1", uuid,
			time);

		purchasedPencilRepository.saveAll(List.of(pencil));

		// when
		List<PurchasedPencilsResponse> purchasedPencils = pencilService.getPurchasedPencils(member);

		assertThat(purchasedPencils).hasSize(0);
	}

	private AcquiredPencil createAcquiredPencilWithTime(LocalDateTime createdAt, String content, Long sharedNoteId,
		Long acquiredQuantity, boolean isRead, AcquiredType type, Member member) {
		return AcquiredPencil.createWithDate(member, content, sharedNoteId, acquiredQuantity, isRead, type, createdAt);
	}

	// private PurchasedPencil createPurchasedPencilWithTime(LocalDateTime createdAt, String content, Long sharedNoteId,
	// 	Long acquiredQuantity, boolean isRead, AcquiredType type, Member member) {
	// 	return PurchasedPencil.createWithDate(member, content, sharedNoteId, acquiredQuantity, isRead, type, createdAt);
	// }
}
