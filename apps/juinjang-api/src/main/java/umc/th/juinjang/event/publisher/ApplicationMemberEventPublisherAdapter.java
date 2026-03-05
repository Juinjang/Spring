package umc.th.juinjang.event.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import umc.th.juinjang.event.SignUpEvent;
import umc.th.juinjang.domain.member.model.Member;

@RequiredArgsConstructor
@Component
public class ApplicationMemberEventPublisherAdapter implements MemberEventPublisher {

  private final ApplicationEventPublisher applicationEventPublisher;

  @Override
  public void publishSignUpEvent(Member member) {
    applicationEventPublisher.publishEvent(SignUpEvent.of(member.getProvider(), member.getNickname(), member.getMemberId()));
  }
}
