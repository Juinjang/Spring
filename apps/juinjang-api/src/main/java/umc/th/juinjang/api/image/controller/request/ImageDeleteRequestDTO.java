package umc.th.juinjang.api.image.controller.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;

public class ImageDeleteRequestDTO {
  @Getter
  public static class DeleteDto {

    @NotEmpty List<Long> imageIdList;
  }
}
