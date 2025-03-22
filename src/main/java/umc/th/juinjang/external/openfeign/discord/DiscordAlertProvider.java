package umc.th.juinjang.external.openfeign.discord;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import umc.th.juinjang.external.openfeign.discord.dto.DiscordAlert;

@RequiredArgsConstructor
@Component
@Slf4j
public class DiscordAlertProvider {

  private final DiscordFeignClient discordFeignClient;

  public void sendAlertToDiscord(String content) {
    try {
      discordFeignClient.sendAlert(DiscordAlert.createAlert(content));
    } catch (FeignException e) {
      log.info(StatusMessage.DISCORD_ALERT_ERROR.getMessage()+ " " +e.getMessage());
    }
  }
}