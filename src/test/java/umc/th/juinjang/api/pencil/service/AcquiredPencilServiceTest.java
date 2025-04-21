package umc.th.juinjang.api.pencil.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import umc.th.juinjang.api.IntegrationTestSupport;
import umc.th.juinjang.api.pencil.service.response.AcquiredPencilResponse;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.member.repository.MemberRepository;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredPencil;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredType;
import umc.th.juinjang.domain.pencil.acquired.repository.AcquiredPencilRepository;
import umc.th.juinjang.testutil.fixture.MemberFixture;

class AcquiredPencilServiceTest extends IntegrationTestSupport {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private AcquiredPencilRepository acquiredPencilRepository;

	@Autowired
	private AcquiredPencilService pencilService;

	@AfterEach
	void tearDown() {
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

	private AcquiredPencil createAcquiredPencilWithTime(LocalDateTime createdAt, String content, Long sharedNoteId,
		Long acquiredQuantity, boolean isRead, AcquiredType type, Member member) {
		return AcquiredPencil.createWithDate(member, content, sharedNoteId, acquiredQuantity, isRead, type, createdAt);
	}
}
