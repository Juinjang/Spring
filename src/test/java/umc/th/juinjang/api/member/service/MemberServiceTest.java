package umc.th.juinjang.api.member.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import umc.th.juinjang.api.IntegrationTestSupport;
import umc.th.juinjang.api.member.service.response.MemberResponseDto;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.member.model.MemberProvider;
import umc.th.juinjang.domain.member.repository.MemberRepository;
import umc.th.juinjang.domain.pencilaccount.repository.PencilAccountRepository;

public class MemberServiceTest extends IntegrationTestSupport {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PencilAccountRepository pencilAccountRepository;

	@Autowired
	private MemberService memberService;

	@AfterEach
	void tearDown() {
		memberRepository.deleteAllInBatch();
		pencilAccountRepository.deleteAllInBatch();
	}

	private final String DEFAULT_EMAIL = "test@naver.com";
	private final String DEFAULT_IMAGE_URL = "https://image.url.com";
	private final String DEFAULT_NICKNAME = "test";
	private final String DEFAULT_INTRODUCTION = "";

	@DisplayName("프로필 조회 시에, (닉네임,이메일,이미지,한줄 소개) 들이 정상적으로 보이는 가?")
	@Test
	void getProfile() {
		// given
		Member member = createAndSaveMember();

		// when
		MemberResponseDto.profileDto profileDto = memberService.getProfile(member);

		// then
		assertThat(profileDto)
			.extracting("nickname", "email", "image", "introduction")
			.containsExactly(DEFAULT_NICKNAME, DEFAULT_EMAIL, DEFAULT_IMAGE_URL, DEFAULT_INTRODUCTION);
	}

	@DisplayName("한 줄 소개 변경이 정상적으로 작동하는 가?")
	@Test
	void patchIntroduction() {
		// given
		Member member = createAndSaveMember();
		String changedIntroduction = "잘 부탁드립니다.!! 여러분";

		// when
		memberService.updateIntroduction(member, changedIntroduction);

		Member updatedMember = memberRepository.findById(member.getMemberId())
			.orElseThrow(() -> new RuntimeException("Member not found"));

		// then
		assertThat(updatedMember.getIntroduction()).isEqualTo(changedIntroduction);
	}

	private Member createDefaultMember() {
		return Member.builder()
			.email(DEFAULT_EMAIL)
			.provider(MemberProvider.KAKAO)
			.kakaoTargetId(91681234L)
			.nickname(DEFAULT_NICKNAME)
			.refreshToken("")
			.refreshTokenExpiresAt(LocalDateTime.now().plusDays(7L))
			.introduction(DEFAULT_INTRODUCTION)
			.imageUrl(DEFAULT_IMAGE_URL)
			.build();
	}

	private Member createAndSaveMember() {
		Member member = createDefaultMember();
		return memberRepository.save(member);
	}
}
