package umc.th.juinjang.api.image.controller.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class ImageUploadRequestDTO {
  @Builder
  @Setter
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ImageDto {
    private Long limjangId;
    private List<MultipartFile> images;
  }
}
