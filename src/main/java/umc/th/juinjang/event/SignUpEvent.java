package umc.th.juinjang.event;

import umc.th.juinjang.domain.member.model.MemberProvider;

public record SignUpEvent(
    MemberProvider memberProvider,
    String name,
    long count
) {
  public static SignUpEvent of(MemberProvider memberProvider, String name, long count) {
    return new SignUpEvent(memberProvider, name, count);
  }
}
