package umc.th.juinjang.api.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import umc.th.juinjang.api.auth.controller.request.AppleInfo;
import umc.th.juinjang.api.auth.controller.request.AppleLoginRequestDto;
import umc.th.juinjang.api.auth.controller.request.AppleSignUpRequestDto;
import umc.th.juinjang.api.auth.controller.request.AppleSignUpRequestVersion2Dto;
import umc.th.juinjang.auth.jwt.JwtService;
import umc.th.juinjang.common.ExceptionHandler;
import umc.th.juinjang.common.exception.handler.MemberHandler;
import umc.th.juinjang.event.publisher.MemberEventPublisher;
import umc.th.juinjang.api.auth.service.response.LoginResponseDto;
import umc.th.juinjang.api.auth.service.response.LoginResponseVersion2Dto;
import umc.th.juinjang.auth.jwt.TokenDto;
import umc.th.juinjang.external.openfeign.apple.AppleClientSecretGenerator;
import umc.th.juinjang.external.openfeign.apple.AppleOAuthProvider;
import umc.th.juinjang.api.auth.controller.request.KakaoLoginRequestDto;
import umc.th.juinjang.api.auth.controller.request.KakaoSignUpRequestDto;
import umc.th.juinjang.api.auth.controller.request.KakaoSignUpRequestVersion2Dto;
import umc.th.juinjang.domain.image.model.Image;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.record.model.Record;
import umc.th.juinjang.domain.member.model.MemberProvider;
import umc.th.juinjang.domain.checklist.repository.ChecklistAnswerRepository;
import umc.th.juinjang.domain.report.repository.ReportRepository;
import umc.th.juinjang.domain.image.repository.ImageRepository;
import umc.th.juinjang.domain.limjang.repository.LimjangPriceRepository;
import umc.th.juinjang.domain.limjang.repository.LimjangRepository;
import umc.th.juinjang.domain.member.repository.MemberRepository;
import umc.th.juinjang.domain.scrap.repository.ScrapRepository;
import umc.th.juinjang.domain.record.repository.RecordRepository;
import umc.th.juinjang.external.openfeign.kakao.KakaoUnlinkClient;
import umc.th.juinjang.external.s3.S3Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static umc.th.juinjang.common.code.status.ErrorStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OAuthService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final AppleClientSecretGenerator appleClientSecretGenerator;
    private final AppleOAuthProvider appleOAuthProvider;
    private final ScrapRepository scrapRepository;
    private final LimjangRepository limjangRepository;
    private final ChecklistAnswerRepository checklistAnswerRepository;
    private final RecordRepository recordRepository;
    private final ImageRepository imageRepository;
    private final ReportRepository reportRepository;
    private final S3Service s3Service;
    private final LimjangPriceRepository limjangPriceRepository;
    private final MemberEventPublisher memberEventPublisher;

    @Autowired
    private KakaoUnlinkClient kakaoUnlinkClient;

    @Value("${security.oauth2.client.registration.kakao.admin-key}")
    private String kakaoAdminKey;

    // 카카오 로그인 (회원가입된 경우)
    // 프론트에서 받은 사용자 정보로 accessToken, refreshToken 발급
    @Transactional
    public LoginResponseDto kakaoLogin(Long targetId, KakaoLoginRequestDto kakaoReqDto) {
        String email = kakaoReqDto.getEmail();
        log.info(kakaoReqDto.getEmail());

        if(email == null)
            throw new MemberHandler(MEMBER_EMAIL_NOT_FOUND);

        Optional<Member> getMemberByEmail = memberRepository.findByEmail(email);
        Optional<Member> getMemberByTargetId = memberRepository.findByKakaoTargetId(targetId);
        Member member = null;


        if(getMemberByEmail.isPresent() && getMemberByTargetId.isEmpty()){
            if(!getMemberByEmail.get().getProvider().equals(MemberProvider.KAKAO)) {  // 이미 회원가입했지만 Kakao가 아닌 다른 소셜 로그인 사용
                throw new MemberHandler(MEMBER_NOT_FOUND_IN_KAKAO);
            } else {    // 잘못된 target_id가 들어왔을때(db에 없는)
                throw new MemberHandler(UNCORRECTED_TARGET_ID);
            }
        } else if(getMemberByEmail.isPresent() && getMemberByTargetId.isPresent()){  // 이미 회원가입한 회원인 경우
            if(!getMemberByEmail.get().getProvider().equals(MemberProvider.KAKAO)) {  // 이미 회원가입했지만 Kakao가 아닌 다른 소셜 로그인 사용
                throw new MemberHandler(MEMBER_NOT_FOUND_IN_KAKAO);
            } else if(getMemberByEmail.get().getMemberId() != getMemberByTargetId.get().getMemberId()) {
                throw new MemberHandler(FAILED_TO_LOGIN);
            }
            member = getMemberByEmail.get();
        } else if(getMemberByEmail.isEmpty() && getMemberByTargetId.isEmpty()){    // 회원가입이 안되어있는 경우 -> 에러 발생. 회원가입 해야 함
            throw new MemberHandler(MEMBER_NOT_FOUND);
        }

        if(member == null) {
            throw new MemberHandler(FAILED_TO_LOGIN);
        }

        // accessToken, refreshToken 발급 후 반환
        return createToken(member);
    }

    // 카카오 로그인 (회원가입 해야하는 경우)
    @Transactional
    public LoginResponseDto kakaoSignUp (Long targetId, KakaoSignUpRequestDto kakaoSignUpReqDto) {
        String email = kakaoSignUpReqDto.getEmail();
        log.info(kakaoSignUpReqDto.getEmail());

        if(email == null)
            throw new MemberHandler(MEMBER_EMAIL_NOT_FOUND);

        Optional<Member> getMember = memberRepository.findByEmail(email);
        Optional<Member> getTargetId = memberRepository.findByKakaoTargetId(targetId);

        Member member = null;

        if(getMember.isPresent() && getTargetId.isEmpty() && getMember.get().getProvider().equals(MemberProvider.APPLE)) {
            throw new MemberHandler(MEMBER_NOT_FOUND_IN_KAKAO);
        } else if(getMember.isPresent() && getTargetId.isPresent()) {
//            if(!getMember.get().getProvider().equals(MemberProvider.KAKAO)) {  // 이미 회원가입했지만 Kakao가 아닌 다른 소셜 로그인 사용
//                throw new MemberHandler(MEMBER_NOT_FOUND_IN_KAKAO);
//            } else
            if((getTargetId.get().getMemberId() != getMember.get().getMemberId())) {
                throw new MemberHandler(FAILED_TO_LOGIN);
            } else if(getMember.get().getProvider().equals(MemberProvider.KAKAO)) {
                throw new MemberHandler(ALREADY_MEMBER);
            }
        } else if (getMember.isPresent() || getTargetId.isPresent()) {  // 둘 중 하나만 존재할 때 실행될 코드
            throw new MemberHandler(FAILED_TO_LOGIN);
        } else if (!getMember.isPresent() && !getTargetId.isPresent()) {   // 두 값 모두 존재하지 않을 때 실행될 코드, 아직 회원가입 하지 않은 회원인 경우
            member = memberRepository.save(
                    Member.builder()
                            .email(email)
                            .provider(MemberProvider.KAKAO)
                            .kakaoTargetId(targetId)
                            .nickname(kakaoSignUpReqDto.getNickname())
                            .refreshToken("")
                            .refreshTokenExpiresAt(LocalDateTime.now())
                            .build()
            );
        }

        if(member == null) {
            throw new MemberHandler(FAILED_TO_SIGNUP);
        }

        // accessToken, refreshToken 발급 후 반환
        publishDiscordAlert(member);
        return createToken(member);
    }

    private void publishDiscordAlert(Member member) {
        memberEventPublisher.publishSignUpEvent(member);
    }

    // accessToken, refreshToken 발급
    @Transactional
    public LoginResponseDto createToken(Member member) {
        String newAccessToken = jwtService.encodeJwtToken(new TokenDto(member.getMemberId()));
        String newRefreshToken = jwtService.encodeJwtRefreshToken(member.getMemberId());

        // DB에 refreshToken 저장
        member.updateRefreshToken(newRefreshToken);
        memberRepository.save(member);

        return new LoginResponseDto(newAccessToken, newRefreshToken, member.getEmail());
    }

    //ver2
    // accessToken, refreshToken 발급
    @Transactional
    public LoginResponseVersion2Dto createTokenVersion2(Member member) {
        String newAccessToken = jwtService.encodeJwtToken(new TokenDto(member.getMemberId()));
        String newRefreshToken = jwtService.encodeJwtRefreshToken(member.getMemberId());

        // DB에 refreshToken 저장
        member.updateRefreshToken(newRefreshToken);

        return new LoginResponseVersion2Dto(newAccessToken, newRefreshToken, member.getEmail(), member.getAgreeVersion());
    }


    // refreshToken으로 accessToken 발급하기
    @Transactional
    public LoginResponseDto regenerateAccessToken(String accessToken, String refreshToken) {
        if(jwtService.validateTokenBoolean(accessToken))  // access token 유효성 검사
            throw new ExceptionHandler(ACCESS_TOKEN_AUTHORIZED);

        if(!jwtService.validateTokenBoolean(refreshToken))  // refresh token 유효성 검사
            throw new ExceptionHandler(REFRESH_TOKEN_UNAUTHORIZED);

        Long memberId = jwtService.getMemberIdFromJwtToken(refreshToken);

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
    
    // 애플 로그인 (회원가입된 경우)
    @Transactional
    public LoginResponseDto appleLogin(AppleLoginRequestDto appleLoginRequest) {
        // email, sub값 추출 후 db에서 해당 email값 그리고 sub값을 가진 유저가 있는지 find
        // 1. 추출한 email, sub 값이 null이면 -> 잘못된 apple token
        // 2. db에서 각각 find한 회원 id가 다르면 에러 (올바르지 않은 정보)
        // 3. db에 email, sub값 둘 다 있으면 재로그인 (혹시 provider가 다르다면 에러)
        // 4. db에 email, sub값 둘 다 없으면 회원가입
        // 탈퇴 처리는 추후에
        log.info("Oauth service 까지 들어옴"+ appleLoginRequest.getIdentityToken());
        AppleInfo appleInfo = jwtService.getAppleAccountId(appleLoginRequest.getIdentityToken().replaceAll("\\n", ""));
        String email = appleInfo.getEmail();
        String sub = appleInfo.getSub();

        if(email == null || sub == null)
            throw new ExceptionHandler(INVALID_APPLE_ID_TOKEN);


        Optional<Member> findSub = memberRepository.findByAppleSub(sub);
        Optional<Member> findEmail = memberRepository.findByEmail(email);

        Member member = null;
        if(findSub.isEmpty() && findEmail.isPresent() && findEmail.get().getProvider().equals(MemberProvider.KAKAO)) {  // 이미 회원가입했지만 Apple 아닌 다른 소셜 로그인 사용
            throw new MemberHandler(MEMBER_NOT_FOUND_IN_APPLE);
        } else if(findSub.isPresent() && findEmail.isPresent()) {  // 재로그인
            if(!findEmail.get().getProvider().equals(MemberProvider.APPLE)) {  // 이미 회원가입했지만 apple이 아닌 다른 소셜 로그인 사용
                throw new MemberHandler(MEMBER_NOT_FOUND_IN_APPLE);
            } else if(findSub.get().getMemberId() != findEmail.get().getMemberId()) {
                throw new MemberHandler(FAILED_TO_LOGIN);
            }
            member = findEmail.get();
        } else if(!findSub.isPresent() && !findEmail.isPresent()) {  // 회원가입이 안되어있는 경우 -> 에러 발생. 회원가입 해야 함
            throw new MemberHandler(MEMBER_NOT_FOUND);
        }

        // accessToken, refreshToken 발급
        if(member == null)
            throw new MemberHandler(MEMBER_NOT_FOUND);
        return createToken(member);
    }

    // 애플 로그인 (회원가입 해야하는 경우)
    @Transactional
    public LoginResponseDto appleSignUp(AppleSignUpRequestDto appleSignUpRequestDto) {
        // email, sub값 추출 후 db에서 해당 email값 그리고 sub값을 가진 유저가 있는지 find
        // 1. 추출한 email, sub 값이 null이면 -> 잘못된 apple token
        // 2. db에서 각각 find한 회원 id가 다르면 에러 (올바르지 않은 정보)
        // 3. db에 email, sub값 둘 다 있으면 재로그인 (혹시 provider가 다르다면 에러)
        // 4. db에 email, sub값 둘 다 없으면 회원가입
        // 탈퇴 처리는 추후에

        AppleInfo appleInfo = jwtService.getAppleAccountId(appleSignUpRequestDto.getIdentityToken());
        String email = appleInfo.getEmail();
        String sub = appleInfo.getSub();

        if(email == null || sub == null)
            throw new ExceptionHandler(INVALID_APPLE_ID_TOKEN);


        Optional<Member> findSub = memberRepository.findByAppleSub(sub);
        Optional<Member> findEmail = memberRepository.findByEmail(email);

        Member member = null;
        if(findSub.isPresent() && findEmail.isPresent() && (findSub.get().getMemberId() == findEmail.get().getMemberId())
                && findSub.get().getProvider().equals(MemberProvider.APPLE)) {  // 이미 회원가입한 회원인 경우 -> 에러 발생
            throw new MemberHandler(ALREADY_MEMBER);
        } else if(!findSub.isPresent() && findEmail.isPresent() && findEmail.get().getProvider().equals(MemberProvider.KAKAO)){
            throw new MemberHandler(MEMBER_NOT_FOUND_IN_APPLE);
        }else if(!findSub.isPresent() && !findEmail.isPresent()) {
            member = memberRepository.save(
                    Member.builder()
                            .email(email)
                            .nickname(appleSignUpRequestDto.getNickname())
                            .provider(MemberProvider.APPLE)
                            .appleSub(sub)
                            .refreshToken("")
                            .refreshTokenExpiresAt(LocalDateTime.now())
                            .build()
            );
            System.out.println("member id : " + member.getMemberId());
            System.out.println("member email : " + member.getEmail());
        }

        // accessToken, refreshToken 발급
        if(member == null)
            throw new MemberHandler(FAILED_TO_LOGIN);

        publishDiscordAlert(member);
        return createToken(member);
    }

    // 카카오 탈퇴 (카카오 연결 끊기)
    @Transactional
    public boolean kakaoWithdraw(Member member, Long kakaoTargetId) {
        ResponseEntity<String> response = kakaoUnlinkClient.unlinkUser("KakaoAK " + kakaoAdminKey, "user_id", kakaoTargetId);

        if (response.getStatusCode().is2xxSuccessful()) { // 성공 처리 로직
            log.info("카카오 탈퇴 성공");
            log.info("member id :: " + member.getMemberId());

            deleteMemberData(member);

            return true;
        } else { // 실패 처리 로직
            return false;
        }
    }

    @Transactional
    public void appleWithdraw(Member member, String code) {

        if(member.getProvider() != MemberProvider.APPLE){
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
        log.info("member id :: " + member.getMemberId());

        deleteMemberData(member);
    }
    @Transactional
    public void deleteAllByLimjangId(Limjang limjang) {
        scrapRepository.deleteByLimjangId(limjang.getLimjangId());
        checklistAnswerRepository.deleteByLimjangId(limjang.getLimjangId());
        limjangPriceRepository.deleteAllByLimjang(limjang);
        List<String> imageList = limjang.getImageList()
                .stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());
        List<String> recordList = limjang.getRecordList()
                .stream()
                .map(Record::getRecordUrl)
                .collect(Collectors.toList());


        deleteFromS3(imageList);
        deleteFromS3(recordList);

        imageRepository.deleteByLimjangId(limjang.getLimjangId());
        recordRepository.deleteByLimjangId(limjang.getLimjangId());
        reportRepository.deleteByLimjangId(limjang.getLimjangId());

    }


    @Transactional
    public void deleteMemberData(Member member) {
        List<Limjang> limjangList = limjangRepository.findLimjangByMemberIdIgnoreDeleted(member.getMemberId());

        for (Limjang limjang : limjangList) {
            deleteAllByLimjangId(limjang);
        }


        if (member.getImageUrl() != null) {
            deleteFromS3(Collections.singletonList(member.getImageUrl()));
        }

        limjangRepository.deleteAllByMemberId(member.getMemberId());
        memberRepository.deleteById(member.getMemberId());
    }

    @Transactional
    public void deleteFromS3(List<String> urlList){
        for (String url : urlList) {
            s3Service.deleteFile(url);
        }
    }

    // V2
    // 카카오
    @Transactional
    public LoginResponseVersion2Dto kakaoLoginVersion2(Long targetId, KakaoLoginRequestDto kakaoReqDto) {
        String email = kakaoReqDto.getEmail();
        log.info(kakaoReqDto.getEmail());

        if(email == null)
            throw new MemberHandler(MEMBER_EMAIL_NOT_FOUND);

        Optional<Member> getMemberByEmail = memberRepository.findByEmail(email);
        Optional<Member> getMemberByTargetId = memberRepository.findByKakaoTargetId(targetId);
        Member member = null;


        if(getMemberByEmail.isPresent() && getMemberByTargetId.isEmpty()){
            if(!getMemberByEmail.get().getProvider().equals(MemberProvider.KAKAO)) {  // 이미 회원가입했지만 Kakao가 아닌 다른 소셜 로그인 사용
                throw new MemberHandler(MEMBER_NOT_FOUND_IN_KAKAO);
            } else {    // 잘못된 target_id가 들어왔을때(db에 없는)
                throw new MemberHandler(UNCORRECTED_TARGET_ID);
            }
        } else if(getMemberByEmail.isPresent() && getMemberByTargetId.isPresent()){  // 이미 회원가입한 회원인 경우
            if(!getMemberByEmail.get().getProvider().equals(MemberProvider.KAKAO)) {  // 이미 회원가입했지만 Kakao가 아닌 다른 소셜 로그인 사용
                throw new MemberHandler(MEMBER_NOT_FOUND_IN_KAKAO);
            } else if(getMemberByEmail.get().getMemberId() != getMemberByTargetId.get().getMemberId()) {
                throw new MemberHandler(FAILED_TO_LOGIN);
            }
            member = getMemberByEmail.get();
        } else if(getMemberByEmail.isEmpty() && getMemberByTargetId.isEmpty()){    // 회원가입이 안되어있는 경우 -> 에러 발생. 회원가입 해야 함
            throw new MemberHandler(MEMBER_NOT_FOUND);
        }

        if(member == null) {
            throw new MemberHandler(FAILED_TO_LOGIN);
        }

        // accessToken, refreshToken 발급 후 반환
        return createTokenVersion2(member);
    }

    // 카카오 로그인 (회원가입 해야하는 경우)
    @Transactional
    public LoginResponseVersion2Dto kakaoSignUpVersion2(Long targetId, KakaoSignUpRequestVersion2Dto kakaoSignUpReqDto) {
        String email = kakaoSignUpReqDto.getEmail();
        log.info(kakaoSignUpReqDto.getEmail());

        if(email == null)
            throw new MemberHandler(MEMBER_EMAIL_NOT_FOUND);

        Optional<Member> getMember = memberRepository.findByEmail(email);
        Optional<Member> getTargetId = memberRepository.findByKakaoTargetId(targetId);

        Member member = null;

        if(getMember.isPresent() && getTargetId.isEmpty() && getMember.get().getProvider().equals(MemberProvider.APPLE)) {
            throw new MemberHandler(MEMBER_NOT_FOUND_IN_KAKAO);
        } else if(getMember.isPresent() && getTargetId.isPresent()) {
//            if(!getMember.get().getProvider().equals(MemberProvider.KAKAO)) {  // 이미 회원가입했지만 Kakao가 아닌 다른 소셜 로그인 사용
//                throw new MemberHandler(MEMBER_NOT_FOUND_IN_KAKAO);
//            } else
            if((getTargetId.get().getMemberId() != getMember.get().getMemberId())) {
                throw new MemberHandler(FAILED_TO_LOGIN);
            } else if(getMember.get().getProvider().equals(MemberProvider.KAKAO)) {
                throw new MemberHandler(ALREADY_MEMBER);
            }
        } else if (getMember.isPresent() || getTargetId.isPresent()) {  // 둘 중 하나만 존재할 때 실행될 코드
            throw new MemberHandler(FAILED_TO_LOGIN);
        } else if (!getMember.isPresent() && !getTargetId.isPresent()) {   // 두 값 모두 존재하지 않을 때 실행될 코드, 아직 회원가입 하지 않은 회원인 경우
            member = memberRepository.save(
                    Member.builder()
                            .email(email)
                            .provider(MemberProvider.KAKAO)
                            .kakaoTargetId(targetId)
                            .nickname(kakaoSignUpReqDto.getNickname())
                            .refreshToken("")
                            .refreshTokenExpiresAt(LocalDateTime.now())
                            .agreeVersion(kakaoSignUpReqDto.getAgreeVersion())
                            .build()
            );
        }

        if(member == null) {
            throw new MemberHandler(FAILED_TO_SIGNUP);
        }

        // accessToken, refreshToken 발급 후 반환
        publishDiscordAlert(member);
        return createTokenVersion2(member);
    }

    // 애플
    public LoginResponseVersion2Dto appleLoginVersion2(AppleLoginRequestDto appleLoginRequest) {
        // email, sub값 추출 후 db에서 해당 email값 그리고 sub값을 가진 유저가 있는지 find
        // 1. 추출한 email, sub 값이 null이면 -> 잘못된 apple token
        // 2. db에서 각각 find한 회원 id가 다르면 에러 (올바르지 않은 정보)
        // 3. db에 email, sub값 둘 다 있으면 재로그인 (혹시 provider가 다르다면 에러)
        // 4. db에 email, sub값 둘 다 없으면 회원가입

        AppleInfo appleInfo = jwtService.getAppleAccountId(appleLoginRequest.getIdentityToken().replaceAll("\\n", ""));
        String email = appleInfo.getEmail();
        String sub = appleInfo.getSub();

        if(email == null || sub == null)
            throw new ExceptionHandler(INVALID_APPLE_ID_TOKEN);


        Optional<Member> findSub = memberRepository.findByAppleSub(sub);
        Optional<Member> findEmail = memberRepository.findByEmail(email);

        Member member = null;
        if(findSub.isEmpty() && findEmail.isPresent() && findEmail.get().getProvider().equals(MemberProvider.KAKAO)) {  // 이미 회원가입했지만 Apple 아닌 다른 소셜 로그인 사용
            throw new MemberHandler(MEMBER_NOT_FOUND_IN_APPLE);
        } else if(findSub.isPresent() && findEmail.isPresent()) {  // 재로그인
            if(!findEmail.get().getProvider().equals(MemberProvider.APPLE)) {  // 이미 회원가입했지만 apple이 아닌 다른 소셜 로그인 사용
                throw new MemberHandler(MEMBER_NOT_FOUND_IN_APPLE);
            } else if(findSub.get().getMemberId() != findEmail.get().getMemberId()) {
                throw new MemberHandler(FAILED_TO_LOGIN);
            }
            member = findEmail.get();
        } else if(!findSub.isPresent() && !findEmail.isPresent()) {  // 회원가입이 안되어있는 경우 -> 에러 발생. 회원가입 해야 함
            throw new MemberHandler(MEMBER_NOT_FOUND);
        }

        // accessToken, refreshToken 발급
        if(member == null)
            throw new MemberHandler(MEMBER_NOT_FOUND);
        return createTokenVersion2(member);

    }

    @Transactional
    public LoginResponseVersion2Dto appleSignUpVersion2(
        AppleSignUpRequestVersion2Dto appleSignUpRequestDto) {
        // email, sub값 추출 후 db에서 해당 email값 그리고 sub값을 가진 유저가 있는지 find
        // 1. 추출한 email, sub 값이 null이면 -> 잘못된 apple token
        // 2. db에서 각각 find한 회원 id가 다르면 에러 (올바르지 않은 정보)
        // 3. db에 email, sub값 둘 다 있으면 재로그인 (혹시 provider가 다르다면 에러)
        // 4. db에 email, sub값 둘 다 없으면 회원가입
        // 탈퇴 처리는 추후에

        AppleInfo appleInfo = jwtService.getAppleAccountId(appleSignUpRequestDto.getIdentityToken());
        String email = appleInfo.getEmail();
        String sub = appleInfo.getSub();

        if(email == null || sub == null)
            throw new ExceptionHandler(INVALID_APPLE_ID_TOKEN);


        Optional<Member> findSub = memberRepository.findByAppleSub(sub);
        Optional<Member> findEmail = memberRepository.findByEmail(email);

        Member member = null;
        if(findSub.isPresent() && findEmail.isPresent() && (findSub.get().getMemberId() == findEmail.get().getMemberId())
                && findSub.get().getProvider().equals(MemberProvider.APPLE)) {  // 이미 회원가입한 회원인 경우 -> 에러 발생
            throw new MemberHandler(ALREADY_MEMBER);
        } else if(!findSub.isPresent() && findEmail.isPresent() && findEmail.get().getProvider().equals(MemberProvider.KAKAO)){
            throw new MemberHandler(MEMBER_NOT_FOUND_IN_APPLE);
        }else if(!findSub.isPresent() && !findEmail.isPresent()) {
            member = memberRepository.save(
                    Member.builder()
                            .email(email)
                            .nickname(appleSignUpRequestDto.getNickname())
                            .provider(MemberProvider.APPLE)
                            .appleSub(sub)
                            .refreshToken("")
                            .refreshTokenExpiresAt(LocalDateTime.now())
                            .agreeVersion(appleSignUpRequestDto.getAgreeVersion())
                            .build()
            );
        }

        // accessToken, refreshToken 발급
        if(member == null)
            throw new MemberHandler(FAILED_TO_LOGIN);

        publishDiscordAlert(member);
        return createTokenVersion2(member);
    }
}
