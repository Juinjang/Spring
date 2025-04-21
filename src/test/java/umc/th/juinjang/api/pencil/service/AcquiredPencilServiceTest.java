package umc.th.juinjang.api.pencil.service;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.groups.Tuple;
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

	@DisplayName("얻은 연필 목록이 없는 경우에는 빈 배열 반환된다.")
	@Test
	void findAcquiredPencils() {
		// given
		Member member = MemberFixture.createDefaultMember();
		memberRepository.save(member);

		// when
		List<AcquiredPencilResponse> list = pencilService.getAcquiredPencils(member);

		// then
		assertThat(list).hasSize(0);
	}

	@DisplayName("얻은 연필 목록이 정상적으로 호출된다")
	@Test
	void getAcquiredPencils() {
		// given
		Member member = MemberFixture.createDefaultMember();
		memberRepository.save(member);

		List<AcquiredPencil> pencils = new ArrayList<>();

		pencils.add(createAcquiredPencil(member, "노트 작성으로 연필 획득", 1L, 10L, false, AcquiredType.NOTE));
		pencils.add(createAcquiredPencil(member, "연필팩 구매로 연필 추가", 2L, 20L, true, AcquiredType.SOLD));
		pencils.add(createAcquiredPencil(member, "매물 판매로 연필 획득", 3L, 30L, false, AcquiredType.SOLD));
		pencils.add(createAcquiredPencil(member, "다른 노트 작성으로 연필 획득", 4L, 15L, true, AcquiredType.NOTE));
		pencils.add(createAcquiredPencil(member, "또 다른 매물 판매로 연필 획득", 5L, 25L, false, AcquiredType.SOLD));

		acquiredPencilRepository.saveAll(pencils);

		// when
		List<AcquiredPencilResponse> foundPencils = pencilService.getAcquiredPencils(member);

		// then
		assertThat(foundPencils).hasSize(5)
			.extracting("content", "sharedNoteId", "acquiredQuantity")
			.containsExactlyInAnyOrder(
				Tuple.tuple("노트 작성으로 연필 획득", 1L, 10L),
				Tuple.tuple("연필팩 구매로 연필 추가", 2L, 20L),
				Tuple.tuple("매물 판매로 연필 획득", 3L, 30L),
				Tuple.tuple("다른 노트 작성으로 연필 획득", 4L, 15L),
				Tuple.tuple("또 다른 매물 판매로 연필 획득", 5L, 25L)
			);
	}

	private AcquiredPencil createAcquiredPencil(Member member, String content, Long sharedNoteId,
		Long acquiredQuantity, boolean isRead, AcquiredType type) {
		return AcquiredPencil.create(member, content, sharedNoteId, acquiredQuantity, isRead, type);
	}

}
