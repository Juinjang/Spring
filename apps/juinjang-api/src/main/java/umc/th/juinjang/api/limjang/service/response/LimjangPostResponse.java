package umc.th.juinjang.api.limjang.service.response;

import java.time.LocalDateTime;
import umc.th.juinjang.domain.limjang.model.Limjang;

public record LimjangPostResponse(Long limjangId, LocalDateTime createdAt) {
  public static LimjangPostResponse of(Limjang limjang) {
    return new LimjangPostResponse(limjang.getLimjangId(), limjang.getCreatedAt());
  }
}