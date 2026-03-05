package umc.th.juinjang.repository.limjang;

import java.time.LocalDateTime;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.member.model.MemberProvider;

public class LimjangFixture {

  public static final long MEMBER_NO = 1L;
  public static final String MEMBER_EMAIL = "test@gmail.com";
  public static final String MEMBER_REFERSH_TOKEN = "abcd";
  public static final LocalDateTime MEMBER_REFERSH_TOKEN_EXPIRES_AT = LocalDateTime.now();
  public static final MemberProvider MEMBER_PROVIDER = MemberProvider.KAKAO;
  public static final Member m1 = Member.builder().build();

  public static Member FIXTURE_MEMBER  = Member.builder(
          ).memberId(MEMBER_NO)
        .email(MEMBER_EMAIL)
        .refreshToken(MEMBER_REFERSH_TOKEN)
        .refreshTokenExpiresAt(MEMBER_REFERSH_TOKEN_EXPIRES_AT)
        .provider(MEMBER_PROVIDER)
        .build();
}
