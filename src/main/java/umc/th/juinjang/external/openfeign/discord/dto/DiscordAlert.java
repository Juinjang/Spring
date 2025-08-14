package umc.th.juinjang.external.openfeign.discord.dto;

public record DiscordAlert(String content) {
  public static DiscordAlert createAlert(String content) {
    return new DiscordAlert(content);
  }
}