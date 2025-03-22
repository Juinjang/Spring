package umc.th.juinjang.api.limjang.service.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.th.juinjang.domain.checklist.model.LimjangCheckListVersion;

public class LimjangDetailResponseDTO {

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class DetailDto {
    private Long limjangId;
    private LimjangCheckListVersion checkListVersion;
    private List<String> images;
    private Integer purposeCode;
    private String nickname;
    private Integer priceType;
    private List<String> priceList;
    private String address;
    private String addressDetail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
  }

}
