package umc.th.juinjang.api.limjang.service.command;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.api.IntegrationTestSupport;
import umc.th.juinjang.api.limjang.controller.parameter.LimjangSortOptions;
import umc.th.juinjang.api.limjang.controller.request.LimjangsDeleteRequest;
import umc.th.juinjang.api.limjang.controller.request.NoteInitRequest;
import umc.th.juinjang.api.limjang.controller.request.NotePatchRequest;
import umc.th.juinjang.api.limjang.service.LimjangCommandService;
import umc.th.juinjang.api.limjang.service.NoteCommandServiceV2;
import umc.th.juinjang.api.limjang.service.NoteQueryServiceV2;
import umc.th.juinjang.api.limjang.service.response.NotePostResponse;
import umc.th.juinjang.api.limjang.service.response.UserNoteGetResponse;
import umc.th.juinjang.api.limjang.service.response.UserNotesGetResponse;
import umc.th.juinjang.common.exception.handler.LimjangHandler;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.limjang.model.LimjangPurpose;
import umc.th.juinjang.domain.limjang.repository.LimjangRepository;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.member.repository.MemberRepository;

@Slf4j
public class NoteCreateTest extends IntegrationTestSupport {

	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private NoteCommandServiceV2 noteCommandService;
	@Autowired
	private LimjangCommandService limjangCommandService;

	@Autowired
	private NoteQueryServiceV2 noteQueryService;
	@Autowired
	private LimjangRepository limjangRepository;

	private Member firstMember;
	private Member secondMember;
	private Member thirdMember;

	@BeforeEach
	void setUp() {
		flushAndTestUsers();
	}

	@DisplayName("새로운 노트를 생성할 때, ")
	@Nested
	class CreateNote {

		private NotePostResponse noteInitResponse;

		@BeforeEach
		void setUpNote() {
			// given
			NoteInitRequest request = new NoteInitRequest(
				LimjangPurpose.RESIDENTIAL_PURPOSE,
				LimjangPropertyType.APARTMENT,
				LimjangPriceType.MONTHLY_RENT,
				"50000",
				"4000"
			);

			// when
			noteInitResponse = noteCommandService.initNote(request, firstMember);
		}

		@DisplayName("올바른 요청이 주어지면, 정상적으로 생성된다")
		@Test
		void createsNote_whenValidRequestIsProvided() {
			// given
			Long createNoteId = noteInitResponse.noteId();

			// when
			Limjang note = limjangRepository.findById(createNoteId).orElseThrow();

			// then
			assertAll(
				() -> assertThat(note.getMemberId().getMemberId()).isEqualTo(firstMember.getMemberId()),
				() -> assertThat(note.getPurpose()).isEqualTo(LimjangPurpose.RESIDENTIAL_PURPOSE),
				() -> assertThat(note.getPropertyType()).isEqualTo(LimjangPropertyType.APARTMENT),
				() -> assertThat(note.getPriceType()).isEqualTo(LimjangPriceType.MONTHLY_RENT),
				() -> assertThat(note.getRecordCount()).isEqualTo(0),
				() -> assertThat(note.getLimjangPrice()).isNotNull(),
				() -> assertThat(note.getNickname()).endsWith(" 매물노트")
			);
		}

		@DisplayName("생성된 노트가 리스트에 정상적으로 노출된다")
		@Test
		void createdNoteIsVisible_inList() {
			// given
			Long createdNoteId = noteInitResponse.noteId();
			log.info("생성된 노트 ID: {}", createdNoteId);

			// when
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

			assertAll(
				() -> assertInitNoteBasicFields(note, createdNoteId),
				() -> assertInitNoteEmptyFields(note)
			);
		}

		@DisplayName("생성된 노트의 상세가 정상적으로 노출된다")
		@Test
		void createdNoteDetailIsVisible() {
			// given
			Long createdNoteId = noteInitResponse.noteId();

			// when
			UserNoteGetResponse response = noteQueryService.findNote(createdNoteId);
			log.info("### response : {} ", response);

			// then
			assertInitNoteBasicFields(response, createdNoteId);
		}

		@DisplayName("생성된 노트가 정상적으로 수정된다")
		@Test
		@Transactional
		void updateNoteSuccess_whenCreateNote() {
			// given
			Long createNoteId = noteInitResponse.noteId();

			NotePatchRequest updateRequest = new NotePatchRequest(
				LimjangPriceType.MONTHLY_RENT,
				"60000",  // 보증금 변경
				"5000",   // 월세 변경
				"서울특별시 강남구 테헤란로 123",  // 도로명 주소
				"101동 101호",  // 상세 주소
				"1168010100",   // 법정동코드
				"강남 아파트 매물노트",  // 닉네임 변경
				"5",      // 층수
				32,       // 평수
				"서울특별시",   // 시도
				"강남구",       // 시군구
				"역삼동",       // 법정동명1
				""              // 법정동명2
			);

			// when
			noteCommandService.updateNoteV2(createNoteId, updateRequest);

			// then
			Limjang updatedNote = limjangRepository.findById(createNoteId).orElseThrow();

			assertAll(
				() -> assertThat(updatedNote.getNickname()).isEqualTo("강남 아파트 매물노트"),
				() -> assertThat(updatedNote.getPriceType()).isEqualTo(LimjangPriceType.MONTHLY_RENT),
				() -> assertThat(updatedNote.getFloor()).isEqualTo("5"),
				() -> assertThat(updatedNote.getPyong()).isEqualTo(32),
				() -> assertThat(updatedNote.getLimjangPrice()).isNotNull(),
				() -> assertThat(updatedNote.getLimjangPrice().getMonthlyRent()).isEqualTo("5000"),
				() -> assertThat(updatedNote.getAddressEntity()).isNotNull(),
				() -> assertThat(updatedNote.getAddressEntity().getRoadAddress()).isEqualTo("서울특별시 강남구 테헤란로 123"),
				() -> assertThat(updatedNote.getAddressEntity().getAddressDetail()).isEqualTo("101동 101호")
			);
		}

		@DisplayName("생성된 노트가 정상적으로 삭제된다")
		@Test
		void deleteNoteSuccessCreateByNote() {
			// given
			long createNoteId = noteInitResponse.noteId();
			List<Long> noteIds = new ArrayList<>();
			noteIds.add(createNoteId);

			// when
			limjangCommandService.deleteLimjangs(LimjangsDeleteRequest.of(noteIds), firstMember);

			// then
			assertThrows(LimjangHandler.class, () -> noteQueryService.findNote(createNoteId));
		}
	}

	// Helper methods
	private void assertInitNoteBasicFields(UserNotesGetResponse.UserNoteResponse note, Long expectedId) {
		assertAll(
			() -> assertThat(note.noteId()).isEqualTo(expectedId),
			() -> assertThat(note.purposeType()).isEqualTo(LimjangPurpose.RESIDENTIAL_PURPOSE),
			() -> assertThat(note.propertyType()).isEqualTo(LimjangPropertyType.APARTMENT),
			() -> assertThat(note.priceType()).isEqualTo(LimjangPriceType.MONTHLY_RENT),
			() -> assertThat(note.isScraped()).isFalse(),
			() -> assertThat(note.name()).endsWith(" 매물노트"),
			() -> assertThat(note.price()).isEqualTo("50000"),
			() -> assertThat(note.monthlyRent()).isEqualTo("4000")
		);
	}

	private void assertInitNoteBasicFields(UserNoteGetResponse note, Long expectedId) {
		assertAll(
			() -> assertThat(note.purposeType()).isEqualTo(LimjangPurpose.RESIDENTIAL_PURPOSE),
			() -> assertThat(note.propertyType()).isEqualTo(LimjangPropertyType.APARTMENT),
			() -> assertThat(note.priceType()).isEqualTo(LimjangPriceType.MONTHLY_RENT),
			() -> assertThat(note.price()).isEqualTo("50000"),
			() -> assertThat(note.monthlyRent()).isEqualTo("4000")
		);
	}

	private void assertInitNoteEmptyFields(UserNotesGetResponse.UserNoteResponse note) {
		assertAll(
			() -> assertThat(note.address()).isNull(),
			() -> assertThat(note.shortAddress()).isNull(),
			() -> assertThat(note.pyong()).isNull(),
			() -> assertThat(note.floor()).isNull(),
			() -> assertThat(note.rate()).isNull(),
			() -> assertThat(note.imageUrl()).isEmpty()
		);
	}

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
}