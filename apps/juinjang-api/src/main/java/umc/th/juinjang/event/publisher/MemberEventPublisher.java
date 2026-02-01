package umc.th.juinjang.event.publisher;

import umc.th.juinjang.domain.member.model.Member;

public interface MemberEventPublisher {
  void publishSignUpEvent(Member member);
}
