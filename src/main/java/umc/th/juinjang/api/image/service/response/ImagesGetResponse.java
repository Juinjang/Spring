package umc.th.juinjang.api.image.service.response;

import java.util.List;
import umc.th.juinjang.domain.image.model.Image;

public record ImagesGetResponse (List<ImageResponse> images) {
  record ImageResponse(Long imageId, String imageUrl) {
    static ImageResponse of(Long imageId, String imageUrl) {
      return new ImageResponse(imageId, imageUrl);
    }
  }

  public static ImagesGetResponse of(List<Image> images) {
    return new ImagesGetResponse(images.stream().map(it -> ImageResponse.of(it.getImageId(), it.getImageUrl())).toList());
  }
}


