package umc.th.juinjang.domain.member.model;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;
import umc.th.juinjang.domain.member.repository.MemberRepository;
import umc.th.juinjang.domain.pencilaccount.model.PencilAccount;
import umc.th.juinjang.domain.pencilaccount.repository.PencilAccountRepository;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class MemberTest {
	// 엔티티 테스트 진행

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PencilAccountRepository pencilAccountRepository;

	@AfterEach
	public void tearDown() {
		memberRepository.deleteAll();
		pencilAccountRepository.deleteAll();
	}

	@DisplayName("카카오 회원가입 시, 회원이 정상적으로 저장이 되는 가?")
	@Test
	void createKakaoMember() {
		// given
		String email = "test@example.com";
		Long targetId = 12345678L;
		String nickname = "테스트유저";
		String agreeVersion = "1.0";

		// when
		Member member = Member.createKakaoMember(email, targetId, nickname, agreeVersion);
		memberRepository.saveAndFlush(member);

		// then
		Member savedMember = memberRepository.findById(member.getMemberId()).orElseThrow();
		PencilAccount pencilAccount = savedMember.getAccount();

		assertThat(savedMember).isNotNull();
		assertThat(savedMember)
			.extracting(Member::getKakaoTargetId, Member::getEmail, Member::getNickname, Member::getAgreeVersion)
			.contains(targetId, email, nickname, agreeVersion);
		assertThat(pencilAccount).isNotNull();
		assertThat(pencilAccount)
			.extracting(PencilAccount::getAcquiredBalance, PencilAccount::getTotalBalance,
				PencilAccount::getPurchasedBalance, PencilAccount::getTotalRefundAmount)
			.contains(0L, 0L, 0L, 0L);
	}

	@DisplayName("애플 회원가입 시, 회원이 정상적으로 저장이 되는 가?")
	@Test
	void createAppleMember() {
		// given
		String email = "test@example.com";
		String sub = "qweqeqws";
		String nickname = "테스트유저";
		String agreeVersion = "1.0";

		// when
		Member member = Member.createAppleMember(email, sub, nickname, agreeVersion);
		memberRepository.saveAndFlush(member);

		// then
		Member savedMember = memberRepository.findById(member.getMemberId()).orElseThrow();
		PencilAccount pencilAccount = savedMember.getAccount();

		assertThat(savedMember).isNotNull();
		assertThat(savedMember)
			.extracting(Member::getAppleSub, Member::getEmail, Member::getNickname, Member::getAgreeVersion)
			.contains(sub, email, nickname, agreeVersion);
		assertThat(pencilAccount).isNotNull();
		assertThat(pencilAccount)
			.extracting(PencilAccount::getAcquiredBalance, PencilAccount::getTotalBalance,
				PencilAccount::getPurchasedBalance, PencilAccount::getTotalRefundAmount)
			.contains(0L, 0L, 0L, 0L);
	}
}
