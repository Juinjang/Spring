package umc.th.juinjang.auth.jwt;

import static umc.th.juinjang.common.LoggerProvider.registerUserId;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import umc.th.juinjang.common.ExceptionHandler;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.external.openfeign.apple.AppleClient;
import umc.th.juinjang.api.auth.controller.request.AppleInfo;
import umc.th.juinjang.domain.member.repository.MemberRepository;
import umc.th.juinjang.external.openfeign.apple.ApplePublicKeyGenerator;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String JWT_SECRET;
    @Value ("${jwt.access-token-expiration}")
    private Long ACCESS_TOKEN_EXPIRE_TIME;
    @Value ("${jwt.refresh-token-expiration}")
    private Long REFRESH_TOKEN_EXPIRE_TIME;

    private final UserDetailServiceImpl userDetailService;

    @Autowired
    private final AppleClient appleAuthClient;
    private MemberRepository memberRepository;

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final ApplePublicKeyGenerator applePublicKeyGenerator;

    // access token 생성
    public String encodeJwtToken(TokenDto tokenDto) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("juinjang")
                .setIssuedAt(now)
                .setSubject(tokenDto.getMemberId().toString())
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .claim("memberId", tokenDto.getMemberId())
                .signWith(SignatureAlgorithm.HS256,
                        Base64.getEncoder().encodeToString(("" + JWT_SECRET).getBytes(
                                StandardCharsets.UTF_8)))
                .compact();
    }

    // refresh token 생성
    public String encodeJwtRefreshToken(Long memberId) {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setSubject(memberId.toString())
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .claim("memberId", memberId)
                .claim("roles", "USER")
                .signWith(SignatureAlgorithm.HS256,
                        Base64.getEncoder().encodeToString(("" + JWT_SECRET).getBytes(
                                StandardCharsets.UTF_8)))
                .compact();
    }

    // JWT 토큰 으로부터 memberId 추출
    public Long getMemberIdFromJwtToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(Base64.getEncoder().encodeToString(("" + JWT_SECRET).getBytes(
                            StandardCharsets.UTF_8)))
                    .parseClaimsJws(token)
                    .getBody();
            return Long.parseLong(claims.getSubject());
        } catch(Exception e) {
            throw new JwtException(e.getMessage());
        }
    }


    // Autorization : Bearer에서 token 추출 (refreshToken, accessToken 포함)
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtAuthenticationFilter.AUTHORIZATION_HEADER);
        if(bearerToken == null)
            throw new NullPointerException();
        else if(StringUtils.isNotEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 유효성 + 만료일자 확인 (만료 여부만 확인. 에러 발생 x)
    public Boolean validateTokenBoolean(String token) {
        Date now = new Date();

        try{
            // 주어진 토큰을 파싱하고 검증.
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(Base64.getEncoder().encodeToString(("" + JWT_SECRET).getBytes(StandardCharsets.UTF_8)))
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date(now.getTime()));
        }catch (JwtException e){

            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }

    // JWT 토큰 인증 정보 조회 (토큰 복호화)
    public Authentication getAuthentication(String token) {
        System.out.println(this.getMemberIdFromJwtToken(token));

        UserDetails userDetails = userDetailService.loadUserByUsername(this.getMemberIdFromJwtToken(token).toString());
        registerUserId(String.valueOf(this.getMemberIdFromJwtToken(token)));
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    // 토큰 유효 시간 확인
    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(accessToken).getBody().getExpiration();
        Long now = new Date().getTime();

        return (expiration.getTime() - now);
    }
    
    // 에플 토큰으로부터 id 추춯하기
    //리팩토링 필요 겹치는 부분 많음
    public AppleInfo getAppleAccountId(String identityToken){
        log.info("getAppleid");
        Map<String, String> headers = parseIdentityToken(identityToken);
        PublicKey publicKey = applePublicKeyGenerator.generatePublicKey(headers, appleAuthClient.getAppleAuthPublicKey());

        log.info("다시 돌아옴");
        Claims claims = getTokenClaims(identityToken, publicKey);
        log.info("claims : " + claims.toString());

        // claims 에러 처리
//        if(!claims.is)

        String email = claims.get("email", String.class);
        String sub = claims.get("sub", String.class);
        log.info("email : " + email + "\nsub : " + sub);

        return new AppleInfo(email, sub);
    }

    public Map<String, String> parseIdentityToken(String token) {
        try {
            String header = token.split("\\.")[0];
            return new ObjectMapper().readValue(decodeHeader(header), Map.class);
        } catch (JsonProcessingException e) {
            throw new ExceptionHandler(ErrorStatus.INVALID_APPLE_ID_TOKEN);
        }
    }

    public String decodeHeader(String token) {
        return new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
    }

    public Claims getTokenClaims(String token, PublicKey publicKey) {
        log.info("getTokenClaims --------");
        //여기서 에러남
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
