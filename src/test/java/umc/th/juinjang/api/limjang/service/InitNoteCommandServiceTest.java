package umc.th.juinjang.api.limjang.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.api.IntegrationTestSupport;
import umc.th.juinjang.api.limjang.controller.parameter.LimjangSortOptions;
import umc.th.juinjang.api.limjang.controller.request.NoteInitRequest;
import umc.th.juinjang.api.limjang.service.response.NotePostResponse;
import umc.th.juinjang.api.limjang.service.response.UserNotesGetResponse;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.limjang.model.LimjangPurpose;
import umc.th.juinjang.domain.limjang.repository.LimjangRepository;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.member.repository.MemberRepository;

@Slf4j
public class InitNoteCommandServiceTest extends IntegrationTestSupport {
	Member firstMember;
	Member secondMember;
	Member thirdMember;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private NoteCommandServiceV2 noteCommandService;
	@Autowired
	private NoteQueryServiceV2 noteQueryService;
	@Autowired
	private LimjangRepository limjangRepository;

	private void flushAndTestUsers() {
		memberRepository.deleteAll();

		// 첫 번째 멤버 (Apple)
		firstMember = Member.createAppleMember(
			"first@apple.com",
			"apple_sub_001",
			"첫번째유저",
			"1.0.0"
		);
		memberRepository.save(firstMember);

		// 두 번째 멤버 (Kakao)
		secondMember = Member.createKakaoMember(
			"second@kakao.com",
			12345L,
			"두번째유저",
			"1.0.0"
		);
		memberRepository.save(secondMember);

		// 세 번째 멤버 (Apple)
		thirdMember = Member.createAppleMember(
			"third@apple.com",
			"apple_sub_002",
			"세번째유저",
			"1.0.0"
		);
		memberRepository.save(thirdMember);

		memberRepository.flush();
	}

	@Test
	@DisplayName("UX 리팩토링을 위해, 새로운 노트 생성이 정상적으로 작동하는 가 ?")
	void initNote() {
		flushAndTestUsers();

		NoteInitRequest request = new NoteInitRequest(
			LimjangPurpose.RESIDENTIAL_PURPOSE,
			LimjangPropertyType.APARTMENT,
			LimjangPriceType.MONTHLY_RENT,
			"50000",
			"4000"
		);
		NotePostResponse response = noteCommandService.initNote(request, firstMember);
		Long createNoteId = response.noteId();

		Limjang note = limjangRepository.findById(createNoteId).orElseThrow();
		assertThat(note).extracting(
			"memberId.memberId",
			"purpose",
			"propertyType",
			"priceType",
			"deleted",
			"recordCount"
		).containsExactly(
			firstMember.getMemberId(),
			LimjangPurpose.RESIDENTIAL_PURPOSE,
			LimjangPropertyType.APARTMENT,
			LimjangPriceType.MONTHLY_RENT,
			false,
			0
		);
		assertThat(note).extracting("limjangPrice").isNotNull();
		assertThat(note).extracting("nickname").satisfies(n -> assertThat((String)n).endsWith(" 매물노트"));
	}

	@Test
	@DisplayName("initNote로 생성된 노트가 리스트에 정상 노출")
	void initNoteAreVisibleInList() {
		// given
		flushAndTestUsers();
		NoteInitRequest request = createInitRequest();

		// when
		log.info("=== 노트 생성 시작 ===");
		Long createdNoteId = noteCommandService.initNote(request, firstMember).noteId();
		log.info("생성된 노트 ID: {}", createdNoteId);

		log.info("=== 노트 조회 시작 ===");
		UserNotesGetResponse response = noteQueryService.findUsersNotes(
			firstMember, LimjangSortOptions.CREATED, ""
		);
		log.info("조회된 노트 개수: {}", response.notes().size());
		// then
		assertThat(response.notes()).hasSize(1);

		UserNotesGetResponse.UserNoteResponse note = response.notes().get(0);
		log.info("=== 노트 정보 ===");
		log.info("### notes : {} ", note);
		log.info("노트 ID: {}", note.noteId());
		log.info("노트 이름: {}", note.name());
		log.info("가격: {}", note.price());
		log.info("월세: {}", note.monthlyRent());

		assertInitNoteBasicFields(note, createdNoteId);
		assertInitNoteEmptyFields(note);
	}

	// 헬퍼 메서드들
	private NoteInitRequest createInitRequest() {
		return new NoteInitRequest(
			LimjangPurpose.RESIDENTIAL_PURPOSE,
			LimjangPropertyType.APARTMENT,
			LimjangPriceType.MONTHLY_RENT,
			"50000",
			"4000"
		);
	}

	private void assertInitNoteBasicFields(UserNotesGetResponse.UserNoteResponse note, Long expectedId) {
		assertThat(note).extracting(
			"noteId", "purposeType", "propertyType", "priceType", "isScraped"
		).containsExactly(
			expectedId,
			LimjangPurpose.RESIDENTIAL_PURPOSE,
			LimjangPropertyType.APARTMENT,
			LimjangPriceType.MONTHLY_RENT,
			false
		);

		assertThat(note.name()).endsWith(" 매물노트");
		assertThat(note.price()).isEqualTo("50000");
		assertThat(note.monthlyRent()).isEqualTo("4000");
	}

	private void assertInitNoteEmptyFields(UserNotesGetResponse.UserNoteResponse note) {
		assertThat(note)
			.extracting("address", "shortAddress", "pyong", "floor", "rate")
			.containsOnlyNulls();

		assertThat(note.imageUrl()).isEmpty();
	}

}
