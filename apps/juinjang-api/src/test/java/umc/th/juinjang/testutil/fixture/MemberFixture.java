package umc.th.juinjang.testutil.fixture;

import java.time.LocalDateTime;

import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.member.model.MemberProvider;

public class MemberFixture {

	public static final String DEFAULT_EMAIL = "test@naver.com";
	public static final String DEFAULT_IMAGE_URL = "https://image.url.com";
	public static final String DEFAULT_NICKNAME = "test";
	public static final String DEFAULT_INTRODUCTION = "";
	public static final Long DEFAULT_KAKAO_ID = 91681234L;

	public static Member createDefaultMember() {
		return createDefaultMemberBuilder().build();
	}

	public static Member.MemberBuilder createDefaultMemberBuilder() {
		return Member.builder()
			.email(DEFAULT_EMAIL)
			.provider(MemberProvider.KAKAO)
			.kakaoTargetId(DEFAULT_KAKAO_ID)
			.nickname(DEFAULT_NICKNAME)
			.refreshToken("")
			.refreshTokenExpiresAt(LocalDateTime.now().plusDays(7L))
			.introduction(DEFAULT_INTRODUCTION)
			.imageUrl(DEFAULT_IMAGE_URL);
	}

	public static Member createMemberWithParams(
		String email,
		Long kakaoTargetId,
		String nickname,
		String introduction,
		String imageUrl) {

		Member.MemberBuilder builder = createDefaultMemberBuilder();

		if (email != null)
			builder.email(email);
		if (kakaoTargetId != null)
			builder.kakaoTargetId(kakaoTargetId);
		if (nickname != null)
			builder.nickname(nickname);
		if (introduction != null)
			builder.introduction(introduction);
		if (imageUrl != null)
			builder.imageUrl(imageUrl);

		return builder.build();
	}
}
