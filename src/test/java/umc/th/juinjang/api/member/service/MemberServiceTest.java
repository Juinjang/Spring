package umc.th.juinjang.api.member.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import umc.th.juinjang.api.member.service.response.MemberResponseDto;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.member.model.MemberProvider;
import umc.th.juinjang.domain.member.repository.MemberRepository;
import umc.th.juinjang.domain.pencilaccount.repository.PencilAccountRepository;
import umc.th.juinjang.testutil.fixture.MemberFixture;

@ActiveProfiles("test")
@SpringBootTest
public class MemberServiceTest {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PencilAccountRepository pencilAccountRepository;

	@Autowired
	private MemberService memberService;

	@AfterEach
	void tearDown() {
		pencilAccountRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
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

	@Nested
	@DisplayName("닉네임 중복 검사")
	class NicknameExistsTest {

		// static 필드로 선언
		private static String existingNickname;

		@BeforeEach
		void setUp() {
			// given - 여러 멤버 데이터 한 번만 설정
			existingNickname = "테스트1";
			String nickname2 = "테스트2";
			String nickname3 = "테스트3";

			Member member1 = MemberFixture.createMemberWithParams(
				"custom1@example.com", 11111111L, existingNickname,
				"안녕하세요", "https://custom.image.url");

			Member member2 = MemberFixture.createMemberWithParams(
				"custom2@example.com", 2222222L, nickname2,
				"안녕하세요", "https://custom.image.url");

			Member member3 = MemberFixture.createMemberWithParams(
				"custom3@example.com", 3333333L, nickname3,
				"안녕하세요", "https://custom.image.url");

			memberRepository.saveAll(List.of(member1, member2, member3));
		}

		@DisplayName("닉네임이 중복되었을 때, 중복 여부를 True 로 반환한다")
		@Test
		void returnsTrueWhenNicknameExists() {
			// when
			boolean result = memberService.isNicknameExists(existingNickname);

			// then
			assertThat(result).isTrue();
		}

		@DisplayName("닉네임이 중복되지 않았을 때, 중복 여부를 False 로 반환한다")
		@Test
		void returnsFalseWhenNicknameDoesNotExist() {
			// given
			String nonExistingNickname = "존재하지않는닉네임";

			// when
			boolean result = memberService.isNicknameExists(nonExistingNickname);

			// then
			assertThat(result).isFalse();
		}
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
