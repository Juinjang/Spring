package umc.th.juinjang.api.image.service;

import umc.th.juinjang.api.image.service.response.ImagesGetResponse;

public interface ImageQueryService {
  ImagesGetResponse getImageList(long limjangId);
}
