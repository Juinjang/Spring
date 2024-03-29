package umc.th.juinjang.service.auth;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import umc.th.juinjang.apiPayload.ExceptionHandler;
import umc.th.juinjang.apiPayload.code.status.ErrorStatus;
import umc.th.juinjang.apiPayload.exception.handler.MemberHandler;
import umc.th.juinjang.model.dto.auth.LoginResponseDto;
import umc.th.juinjang.model.dto.auth.TokenDto;
import umc.th.juinjang.model.dto.auth.kakao.KakaoOAuthToken;
import umc.th.juinjang.model.dto.auth.kakao.KakaoUser;
import umc.th.juinjang.model.entity.Member;
import umc.th.juinjang.model.entity.enums.MemberProvider;
import umc.th.juinjang.repository.limjang.MemberRepository;
import umc.th.juinjang.service.JwtService;

import java.io.IOException;

import java.time.LocalDateTime;
import java.util.Optional;

import static umc.th.juinjang.apiPayload.code.status.ErrorStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    // 카카오 로그인
    private final KakaoOauth kakaoOauth;
    private final HttpServletResponse response;

    // 카카오 로그인
    // 사용자 로그인 페이지 제공 단계 - url 반환
    public void request(String socialLoginType) throws IOException {
        String redirectURL = kakaoOauth.getOauthRedirectURL();

        response.sendRedirect(redirectURL);
    }

    // code -> accessToken 받아오기
    // accessToken -> 사용자 정보 받아오기
    @Transactional
    public LoginResponseDto oauthLogin(String socialLoginType, String code) throws JsonProcessingException {
        // 카카오로 일회성 코드를 보내 액세스 토큰이 담긴 응답객체를 받아옴
        ResponseEntity<String> accessTokenResponse = kakaoOauth.requestAccessToken(code);

        log.info("response.getBody() = " + accessTokenResponse.getBody());

        // 응답 객체가 JSON 형식으로 되어 있으므로, 이를 deserialization 해서 자바 객체에 담음 (+ 로그 출력됨)
        KakaoOAuthToken oAuthToken = kakaoOauth.getAccessToken(accessTokenResponse);

        // 액세스 토큰을 다시 카카오로 보내 카카오에 저장된 사용자 정보가 담긴 응답 객체를 받아옴
        ResponseEntity<String> userInfoResponse = kakaoOauth.requestUserInfo(oAuthToken);

        // 다시 JSON 형식의 응답 객체를 deserialization 해서 자바 객체에 담음
        KakaoUser kakaoUser = kakaoOauth.getUserInfo(userInfoResponse);

        String email = kakaoUser.getKakao_account().getEmail();
        log.info(kakaoUser.getKakao_account().getEmail());

        if(email == null)
            throw new MemberHandler(ErrorStatus.MEMBER_EMAIL_NOT_FOUND);

        Optional<Member> getMember = memberRepository.findByEmail(email);
        Member member;
        if(getMember.isPresent()){  // 이미 회원가입한 회원인 경우
            member = getMember.get();
            if(!member.getProvider().equals(MemberProvider.KAKAO))   // 이미 회원가입했지만 Kakao가 아닌 다른 소셜 로그인 사용
                throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND_IN_KAKAO);
        } else {    // 아직 회원가입 하지 않은 회원인 경우
            member = memberRepository.save(
                    Member.builder()
                            .email(email)
                            .provider(MemberProvider.KAKAO)
                            .refreshToken("")
                            .refreshTokenExpiresAt(LocalDateTime.now())
                            .build()
            );
            System.out.println("member id : " + member.getMemberId());
            System.out.println("member email : " + member.getEmail());
        }

        // accessToken, refreshToken 발급
        String newAccessToken = jwtService.encodeJwtToken(new TokenDto(member.getMemberId()));
        String newRefreshToken = jwtService.encodeJwtRefreshToken(member.getMemberId());

        System.out.println("newAccessToken : " + newAccessToken);
        System.out.println("newRefreshToken : " + newRefreshToken);

        // DB에 refreshToken 저장
        member.updateRefreshToken(newRefreshToken);
        memberRepository.save(member);

        System.out.println("member nickname : " + member.getNickname());

        return new LoginResponseDto(newAccessToken, newRefreshToken, member.getNickname());
    }

    // refreshToken으로 accessToken 발급하기
    @Transactional
    public LoginResponseDto regenerateAccessToken(String accessToken, String refreshToken) {
        if(jwtService.validateTokenBoolean(accessToken))  // access token 유효성 검사
            throw new ExceptionHandler(ACCESS_TOKEN_AUTHORIZED);

        if(!jwtService.validateToken(refreshToken))  // refresh token 유효성 검사
            throw new ExceptionHandler(REFRESH_TOKEN_UNAUTHORIZED);

        Long memberId = jwtService.getMemberIdFromJwtToken(refreshToken);
        log.info("memberId : " + memberId);

        Optional<Member> getMember = memberRepository.findById(memberId);
        if(getMember.isEmpty())
            throw new MemberHandler(MEMBER_NOT_FOUND);

        Member member = getMember.get();
        if(!refreshToken.equals(member.getRefreshToken()))
            throw new ExceptionHandler(REFRESH_TOKEN_UNAUTHORIZED);

        String newRefreshToken = jwtService.encodeJwtRefreshToken(memberId);
        String newAccessToken = jwtService.encodeJwtToken(new TokenDto(memberId));

        member.updateRefreshToken(newRefreshToken);
        memberRepository.save(member);

        System.out.println("member nickname : " + member.getNickname());

        return new LoginResponseDto(newAccessToken, newRefreshToken, member.getNickname());
    }

    // 로그아웃
    @Transactional
    public String logout(String refreshToken) {
        Optional<Member> getMember = memberRepository.findByRefreshToken(refreshToken);
        if(getMember.isEmpty())
            throw new MemberHandler(MEMBER_NOT_FOUND);

        Member member = getMember.get();
        if(member.getRefreshToken().equals(""))
            throw new MemberHandler(ALREADY_LOGOUT);

        member.refreshTokenExpires();
        memberRepository.save(member);

        return "로그아웃 성공";
    }
}