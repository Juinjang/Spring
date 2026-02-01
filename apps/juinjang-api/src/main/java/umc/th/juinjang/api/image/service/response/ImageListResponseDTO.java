package umc.th.juinjang.api.image.service.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ImageListResponseDTO {
  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ImagesListDTO {
    List<ImageDTO> images;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ImageDTO{
    Long imageId;
    String imageUrl;
  }
}
