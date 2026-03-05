package umc.th.juinjang.api.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import umc.th.juinjang.api.IntegrationTestSupport;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.member.model.MemberStatus;
import umc.th.juinjang.domain.member.repository.MemberRepository;
import umc.th.juinjang.external.openfeign.kakao.KakaoUnlinkClient;

public class OauthServiceTest extends IntegrationTestSupport {

	@MockBean
	private KakaoUnlinkClient kakaoUnlinkClient;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private OAuthServiceV2 oauthService;

	@Test
	@DisplayName("카카오 탈퇴 성공 - 카카오 연결 끊기 성공 시 회원 상태가 WITHDRAWN으로 변경")
	void kakaoWithdraw_Success() {
		// given
		Long testTargetId = 123456789L;

		Member testMember = Member.createKakaoMember(
			"test@example.com",
			testTargetId,
			"테스트유저",
			"1.0"
		);
		memberRepository.save(testMember);

		ResponseEntity<String> successResponse = new ResponseEntity<>("success", HttpStatus.OK);

		BDDMockito.when(
			kakaoUnlinkClient.unlinkUser(any(), any(), any())
		).thenReturn(successResponse);

		// when
		boolean result = oauthService.kakaoWithdraw(testMember, testTargetId);

		// then
		Member updatedMember = memberRepository.findById(testMember.getMemberId()).orElseThrow();
		assertThat(updatedMember)
			.extracting(Member::getMemberId, Member::getStatus, Member::getKakaoTargetId, Member::getNickname,
				Member::getDeletedAt)
			.containsExactly(testMember.getMemberId(), MemberStatus.WITHDRAWN, null, null,
				updatedMember.getDeletedAt());
	}

}
