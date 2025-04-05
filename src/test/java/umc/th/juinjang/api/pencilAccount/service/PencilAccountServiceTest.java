package umc.th.juinjang.api.pencilAccount.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.PencilAccountHandler;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.member.model.MemberProvider;
import umc.th.juinjang.domain.member.repository.MemberRepository;

@ActiveProfiles("test")
@SpringBootTest
public class PencilAccountServiceTest {

	private final String DEFAULT_EMAIL = "test@naver.com";
	private final String DEFAULT_IMAGE_URL = "https://image.url.com";
	private final String DEFAULT_NICKNAME = "test";
	private final String DEFAULT_INTRODUCTION = "";

	@Autowired
	private PencilAccountService pencilAccountService;

	@Autowired
	private MemberRepository memberRepository;

	@DisplayName("보유한 총 연필의 개수를 정상적으로 불러오는 가?")
	@Test
	void getTotalPencilAmountByMember() {
		// given
		Member member = createAndSaveMember();

		// when
		long totalBalance = pencilAccountService.getTotalPencilAmountByMember(member);

		// then
		assertThat(totalBalance).isEqualTo(0L);
	}

	@DisplayName("연필 계좌가 존재하지 않는 경우 에러가 발생하는 가 ?")
	@Test
	void getTotalPencilAmountByMemberWithoutPencilAccount() {
		// given
		Member member = createAndSaveMemberWithoutAccount();

		// when
		assertThatThrownBy(() -> pencilAccountService.getTotalPencilAmountByMember(member))
			.isInstanceOf(PencilAccountHandler.class)
			.satisfies(exception -> {
				PencilAccountHandler pencilException = (PencilAccountHandler)exception;
				assertThat(pencilException.getErrorReasonHttpStatus().getMessage())
					.isEqualTo(ErrorStatus.PENCIL_ACCOUNT_NOT_FOUND.getMessage());
			});
	}

	private Member createAndSaveMember() {
		Member member = Member.createKakaoMember(
			DEFAULT_EMAIL,
			1234567L,
			DEFAULT_NICKNAME,
			"1.0"
		);
		return memberRepository.save(member);
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

	private Member createAndSaveMemberWithoutAccount() {
		Member member = createDefaultMember();
		return memberRepository.save(member);
	}
}
