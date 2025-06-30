package umc.th.juinjang.api.auth.service;

import static umc.th.juinjang.common.code.status.ErrorStatus.*;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.api.auth.controller.request.AppleInfo;
import umc.th.juinjang.api.auth.controller.request.AppleLoginRequestDto;
import umc.th.juinjang.api.auth.controller.request.AppleSignUpRequestDto;
import umc.th.juinjang.api.auth.controller.request.AppleSignUpRequestVersion2Dto;
import umc.th.juinjang.api.auth.controller.request.KakaoLoginRequestDto;
import umc.th.juinjang.api.auth.controller.request.KakaoSignUpRequestDto;
import umc.th.juinjang.api.auth.controller.request.KakaoSignUpRequestVersion2Dto;
import umc.th.juinjang.api.auth.service.response.LoginResponseDto;
import umc.th.juinjang.api.auth.service.response.LoginResponseVersion2Dto;
import umc.th.juinjang.auth.jwt.JwtService;
import umc.th.juinjang.auth.jwt.TokenDto;
import umc.th.juinjang.common.ExceptionHandler;
import umc.th.juinjang.common.exception.handler.MemberHandler;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.member.model.MemberProvider;
import umc.th.juinjang.domain.member.model.MemberStatus;
import umc.th.juinjang.domain.member.repository.MemberRepository;
import umc.th.juinjang.event.publisher.MemberEventPublisher;
import umc.th.juinjang.external.openfeign.apple.AppleClientSecretGenerator;
import umc.th.juinjang.external.openfeign.apple.AppleOAuthProvider;
import umc.th.juinjang.external.openfeign.kakao.KakaoUnlinkClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthServiceV2 {

	private final MemberRepository memberRepository;
	private final JwtService jwtService;
	private final AppleClientSecretGenerator appleClientSecretGenerator;
	private final AppleOAuthProvider appleOAuthProvider;
	private final MemberEventPublisher memberEventPublisher;

	@Autowired
	private KakaoUnlinkClient kakaoUnlinkClient;

	@Value("${security.oauth2.client.registration.kakao.admin-key}")
	private String kakaoAdminKey;

	@Transactional
	public LoginResponseDto kakaoLogin(Long targetId, KakaoLoginRequestDto dto) {
		Optional<Member> member =
			memberRepository.findByEmailAndKakaoTargetIdAndStatus(
				dto.getEmail(),
				targetId,
				MemberStatus.ACTIVE
			);

		return member.map(this::createToken)
			.orElseThrow(() -> handleKakaoLoginError(dto.getEmail(), targetId));
	}

	@Transactional
	public LoginResponseDto kakaoSignUp(Long targetId, KakaoSignUpRequestDto kakaoLoginRequestDto) {
		Optional<Member> member =
			memberRepository.findByEmailAndKakaoTargetIdAndStatus(
				kakaoLoginRequestDto.getEmail(),
				targetId,
				MemberStatus.ACTIVE
			);

		if (member.isPresent()) {
			throw new MemberHandler(ALREADY_MEMBER);
		} else {
			Member newMember = memberRepository.save(
				Member.createKakaoMember(
					kakaoLoginRequestDto.getEmail(),
					targetId,
					kakaoLoginRequestDto.getNickname(),
					null
				)
			);

			publishDiscordAlert(newMember);
			return createToken(newMember);
		}
	}

	@Transactional
	public LoginResponseVersion2Dto kakaoLoginVersion2(Long targetId, KakaoLoginRequestDto dto) {
		Optional<Member> member =
			memberRepository.findByEmailAndKakaoTargetIdAndStatus(
				dto.getEmail(),
				targetId,
				MemberStatus.ACTIVE
			);

		return member.map(this::createTokenVersion2)
			.orElseThrow(() -> handleKakaoLoginError(dto.getEmail(), targetId));
	}

	@Transactional
	public LoginResponseVersion2Dto kakaoSignUpVersion2(Long targetId, KakaoSignUpRequestVersion2Dto dto) {
		Optional<Member> member =
			memberRepository.findByEmailAndKakaoTargetIdAndStatus(
				dto.getEmail(),
				targetId,
				MemberStatus.ACTIVE
			);

		if (member.isPresent()) {
			throw new MemberHandler(ALREADY_MEMBER);
		} else {
			Member newMember = memberRepository.save(
				Member.createKakaoMember(
					dto.getEmail(),
					targetId,
					dto.getNickname(),
					null
				)
			);

			publishDiscordAlert(newMember);
			return createTokenVersion2(newMember);
		}
	}

	@Transactional
	public String logout(String refreshToken) {
		Optional<Member> getMember = memberRepository.findByRefreshToken(refreshToken);
		if (getMember.isEmpty())
			throw new MemberHandler(MEMBER_NOT_FOUND);

		Member member = getMember.get();
		if (member.getRefreshToken().equals(""))
			throw new MemberHandler(ALREADY_LOGOUT);

		member.refreshTokenExpires();
		memberRepository.save(member);

		return "로그아웃 성공";
	}

	@Transactional
	public LoginResponseDto regenerateAccessToken(String accessToken, String refreshToken) {
		if (jwtService.validateTokenBoolean(accessToken))  // access token 유효성 검사
			throw new ExceptionHandler(ACCESS_TOKEN_AUTHORIZED);

		if (!jwtService.validateTokenBoolean(refreshToken))  // refresh token 유효성 검사
			throw new ExceptionHandler(REFRESH_TOKEN_UNAUTHORIZED);

		Long memberId = jwtService.getMemberIdFromJwtToken(refreshToken);

		Optional<Member> getMember = memberRepository.findById(memberId);
		if (getMember.isEmpty())
			throw new MemberHandler(MEMBER_NOT_FOUND);

		Member member = getMember.get();
		if (!refreshToken.equals(member.getRefreshToken()))
			throw new ExceptionHandler(REFRESH_TOKEN_UNAUTHORIZED);

		String newRefreshToken = jwtService.encodeJwtRefreshToken(memberId);
		String newAccessToken = jwtService.encodeJwtToken(new TokenDto(memberId));

		member.updateRefreshToken(newRefreshToken);
		memberRepository.save(member);

		return new LoginResponseDto(newAccessToken, newRefreshToken, member.getNickname());
	}

	@Transactional
	public LoginResponseDto createToken(Member member) {
		String newAccessToken = jwtService.encodeJwtToken(new TokenDto(member.getMemberId()));
		String newRefreshToken = jwtService.encodeJwtRefreshToken(member.getMemberId());

		// DB에 refreshToken 저장
		member.updateRefreshToken(newRefreshToken);
		memberRepository.save(member);

		return new LoginResponseDto(newAccessToken, newRefreshToken, member.getEmail());
	}

	@Transactional
	public LoginResponseVersion2Dto createTokenVersion2(Member member) {
		String newAccessToken = jwtService.encodeJwtToken(new TokenDto(member.getMemberId()));
		String newRefreshToken = jwtService.encodeJwtRefreshToken(member.getMemberId());

		// DB에 refreshToken 저장
		member.updateRefreshToken(newRefreshToken);

		return new LoginResponseVersion2Dto(newAccessToken, newRefreshToken, member.getEmail(),
			member.getAgreeVersion());
	}

	private void publishDiscordAlert(Member member) {
		memberEventPublisher.publishSignUpEvent(member);
	}

	@Transactional
	public LoginResponseDto appleLogin(AppleLoginRequestDto request) {
		log.info("Oauth service 까지 들어옴{}", request.getIdentityToken());
		AppleInfo appleInfo = jwtService.getAppleAccountId(request.getIdentityToken().replaceAll("\\n", ""));
		String email = appleInfo.getEmail();
		String sub = appleInfo.getSub();

		Optional<Member> member =
			memberRepository.findByEmailAndAppleSubAndStatus(
				email,
				sub,
				MemberStatus.ACTIVE
			);

		return member.map(this::createToken)
			.orElseThrow(() -> handleAppleLoginError(email, sub));
	}

	@Transactional
	public LoginResponseDto appleSignUp(AppleSignUpRequestDto request) {
		AppleInfo appleInfo = jwtService.getAppleAccountId(request.getIdentityToken());
		String email = appleInfo.getEmail();
		String sub = appleInfo.getSub();

		Optional<Member> member =
			memberRepository.findByEmailAndAppleSubAndStatus(
				email,
				sub,
				MemberStatus.ACTIVE
			);

		if (member.isPresent()) {
			throw new MemberHandler(ALREADY_MEMBER);
		} else {
			Member newMember = memberRepository.save(
				Member.createAppleMember(
					email,
					sub,
					request.getNickname(),
					null
				)
			);

			publishDiscordAlert(newMember);
			return createToken(newMember);
		}
	}

	@Transactional
	public LoginResponseVersion2Dto appleLoginVersion2(AppleLoginRequestDto request) {
		AppleInfo appleInfo = jwtService.getAppleAccountId(request.getIdentityToken().replaceAll("\\n", ""));
		String email = appleInfo.getEmail();
		String sub = appleInfo.getSub();

		if (email == null || sub == null)
			throw new ExceptionHandler(INVALID_APPLE_ID_TOKEN);

		Optional<Member> member =
			memberRepository.findByEmailAndAppleSubAndStatus(
				email,
				sub,
				MemberStatus.ACTIVE
			);

		return member.map(this::createTokenVersion2)
			.orElseThrow(() -> handleAppleLoginError(email, sub));
	}

	@Transactional
	public LoginResponseVersion2Dto appleSignUpVersion2(AppleSignUpRequestVersion2Dto request) {
		AppleInfo appleInfo = jwtService.getAppleAccountId(request.getIdentityToken());
		String email = appleInfo.getEmail();
		String sub = appleInfo.getSub();

		if (email == null || sub == null)
			throw new ExceptionHandler(INVALID_APPLE_ID_TOKEN);

		Optional<Member> member =
			memberRepository.findByEmailAndAppleSubAndStatus(
				email,
				sub,
				MemberStatus.ACTIVE
			);

		if (member.isPresent()) {
			throw new MemberHandler(ALREADY_MEMBER);
		} else {
			Member newMember = memberRepository.save(
				Member.createAppleMember(
					email,
					sub,
					request.getNickname(),
					request.getAgreeVersion()
				)
			);

			publishDiscordAlert(newMember);
			return createTokenVersion2(newMember);
		}
	}

	@Transactional
	public boolean kakaoWithdraw(Member member, Long targetId) {
		ResponseEntity<String> response = kakaoUnlinkClient.unlinkUser("KakaoAK " + kakaoAdminKey, "user_id",
			targetId);

		if (response.getStatusCode().is2xxSuccessful()) { // 성공 처리 로직
			log.info("카카오 탈퇴 성공");
			log.info("member id :: {}", member.getMemberId());

			member.kakaoWithdraw();
			memberRepository.save(member);

			return true;
		} else { // 실패 처리 로직
			return false;
		}
	}

	@Transactional
	public void appleWithdraw(Member member, String code) {
		if (member.getProvider() != MemberProvider.APPLE) {
			throw new MemberHandler(MEMBER_NOT_FOUND_IN_APPLE);
		}
		try {
			String clientSecret = appleClientSecretGenerator.generateClientSecret();
			String refreshToken = appleOAuthProvider.getAppleRefreshToken(code, clientSecret);
			appleOAuthProvider.requestRevoke(refreshToken, clientSecret);
		} catch (Exception e) {
			throw new MemberHandler(FAILED_TO_LOAD_PRIVATE_KEY);
		}
		log.info("애플 탈퇴 성공");
		log.info("member id :: {}", member.getMemberId());

		member.appleWithdraw();
		memberRepository.save(member);
	}

	private MemberHandler handleKakaoLoginError(String email, Long targetId) {
		if (email == null || email.trim().isEmpty()) {
			return new MemberHandler(MEMBER_EMAIL_NOT_FOUND);
		}

		Optional<Member> getMemberByEmail = memberRepository.findByEmail(email);
		Optional<Member> getMemberByTargetId = memberRepository.findByKakaoTargetId(targetId);

		if (getMemberByEmail.isPresent()) {
			Member foundMember = getMemberByEmail.get();

			if (!foundMember.getProvider().equals(MemberProvider.KAKAO)) {
				return new MemberHandler(MEMBER_NOT_FOUND_IN_KAKAO);
			}
		}

		if (getMemberByTargetId.isPresent()) {
			Member foundMember = getMemberByTargetId.get();
			if (!foundMember.getProvider().equals(MemberProvider.KAKAO)) {
				return new MemberHandler(MEMBER_NOT_FOUND_IN_KAKAO);
			}
		}

		return new MemberHandler(MEMBER_NOT_FOUND);
	}

	private MemberHandler handleAppleLoginError(String email, String sub) {
		if (email == null || email.trim().isEmpty()) {
			return new MemberHandler(MEMBER_EMAIL_NOT_FOUND);
		}

		if (sub == null || sub.trim().isEmpty()) {
			return new MemberHandler(INVALID_APPLE_ID_TOKEN);
		}

		Optional<Member> getMemberByEmail = memberRepository.findByEmail(email);
		Optional<Member> getMemberBySub = memberRepository.findByAppleSub(sub);

		if (getMemberByEmail.isPresent()) {
			Member foundMember = getMemberByEmail.get();

			if (!foundMember.getProvider().equals(MemberProvider.APPLE)) {
				return new MemberHandler(MEMBER_NOT_FOUND_IN_APPLE);
			}
		}

		if (getMemberBySub.isPresent()) {
			Member foundMember = getMemberBySub.get();

			if (!foundMember.getProvider().equals(MemberProvider.APPLE)) {
				return new MemberHandler(MEMBER_NOT_FOUND_IN_APPLE);
			}

			if (!foundMember.getEmail().equals(email)) {
				return new MemberHandler(FAILED_TO_LOGIN);
			}
		}

		// 둘 다 찾아지지 않는 경우 - 회원가입이 필요
		return new MemberHandler(MEMBER_NOT_FOUND);
	}
}
