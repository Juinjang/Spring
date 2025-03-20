package umc.th.juinjang.external.openfeign.discord;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum StatusMessage {
  DISCORD_ALERT_ERROR("discord 알림 수신 중 오류가 발생했습니다."),
  DISCORD_ALERT_SUCCESS("discord 알림 수신 성공했습니다.");

  private final String message;
}
