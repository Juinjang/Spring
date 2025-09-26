package umc.th.juinjang.api.limjang.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import umc.th.juinjang.api.IntegrationTestSupport;
import umc.th.juinjang.api.limjang.controller.request.NoteInitRequest;
import umc.th.juinjang.api.limjang.service.response.NotePostResponse;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.limjang.model.LimjangPurpose;
import umc.th.juinjang.domain.limjang.repository.LimjangRepository;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.member.repository.MemberRepository;

class NoteCommandServiceV2Test extends IntegrationTestSupport {

	Member firstMember;
	Member secondMember;
	Member thirdMember;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private NoteCommandServiceV2 noteCommandServiceV2;

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
	void initNote() {
		flushAndTestUsers();

		NoteInitRequest request = new NoteInitRequest(
			LimjangPurpose.RESIDENTIAL_PURPOSE,
			LimjangPropertyType.APARTMENT,
			LimjangPriceType.MONTHLY_RENT,
			"50000",
			"4000"
		);
		NotePostResponse response = noteCommandServiceV2.initNote(request, firstMember);
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
}