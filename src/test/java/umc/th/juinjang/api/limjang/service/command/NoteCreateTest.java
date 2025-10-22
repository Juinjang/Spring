package umc.th.juinjang.api.limjang.service.command;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.api.IntegrationTestSupport;
import umc.th.juinjang.api.limjang.controller.parameter.LimjangSortOptions;
import umc.th.juinjang.api.limjang.controller.request.NotePostRequest;
import umc.th.juinjang.api.limjang.service.NoteCommandServiceV2;
import umc.th.juinjang.api.limjang.service.NoteQueryServiceV2;
import umc.th.juinjang.api.limjang.service.response.NotePostResponse;
import umc.th.juinjang.api.limjang.service.response.UserNoteGetResponse;
import umc.th.juinjang.api.limjang.service.response.UserNotesGetResponse;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.limjang.model.LimjangPurpose;
import umc.th.juinjang.domain.limjang.repository.LimjangRepository;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.member.repository.MemberRepository;

@Slf4j
class NoteCreateTest extends IntegrationTestSupport {

	Member firstMember;
	Member secondMember;
	Member thirdMember;

	NotePostResponse response;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private NoteCommandServiceV2 noteCommandServiceV2;

	@Autowired
	private LimjangRepository limjangRepository;
	@Autowired
	private NoteQueryServiceV2 noteQueryServiceV2;

	@BeforeEach
	void setup() {
		// given
		flushAndTestUsers();

		NotePostRequest request = new NotePostRequest(
			LimjangPurpose.RESIDENTIAL_PURPOSE,
			LimjangPropertyType.APARTMENT,
			LimjangPriceType.MONTHLY_RENT,
			"50000",
			"4000",
			"서울특별시 강남구 테헤란로 123",      // roadAddress (필수)
			"101호",                              // addressDetail
			"1168010100",                         // bcode (필수)
			"우리집 매물노트",                     // nickname (필수)
			"5",                                  // floor (필수)
			30,                                   // pyong
			"서울특별시",                          // sido
			"강남구",                             // sigungu
			"역삼동",                             // bname1
			""                                    // bname2
		);

		response = noteCommandServiceV2.createNote(request, firstMember);
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

	@Test
	@DisplayName("createNote 를 통해, note를 만들었을 경우에, 상세가 정상적으로 노출되는 가?")
	void isDetailShowByCreateNote() {
		Long createNoteId = response.noteId();

		// given
		UserNoteGetResponse note = noteQueryServiceV2.findNote(createNoteId);
		assertThat(note).extracting(
			"purposeType",
			"propertyType",
			"priceType"
		).containsExactly(
			LimjangPurpose.RESIDENTIAL_PURPOSE,
			LimjangPropertyType.APARTMENT,
			LimjangPriceType.MONTHLY_RENT
		);

	}

	@Test
	@DisplayName("createNote 를 통해, 노트를 만들었을 경우에 리스트가 정상적으로 노출되는 가 ?")
	void createNoteByList() {
		// given
		UserNotesGetResponse response = noteQueryServiceV2.findUsersNotes(firstMember, LimjangSortOptions.CREATED, "");
		log.info("#### NOTES  : {} ", response.notes());
		// then
		assertThat(response.notes()).hasSize(1);
		assertThat(response.notes().get(0)).extracting(
			"purposeType",
			"propertyType",
			"priceType",
			"name"
		).containsExactly(
			LimjangPurpose.RESIDENTIAL_PURPOSE,
			LimjangPropertyType.APARTMENT,
			LimjangPriceType.MONTHLY_RENT,
			"우리집 매물노트"
		);
	}
}