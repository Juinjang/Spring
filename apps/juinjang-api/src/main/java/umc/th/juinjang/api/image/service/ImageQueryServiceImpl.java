package umc.th.juinjang.api.image.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.LimjangHandler;
import umc.th.juinjang.api.image.service.response.ImagesGetResponse;
import umc.th.juinjang.domain.image.model.Image;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.image.repository.ImageRepository;
import umc.th.juinjang.domain.limjang.repository.LimjangRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageQueryServiceImpl implements ImageQueryService {

  private final ImageRepository imageRepository;
  private final LimjangRepository limjangRepository;

  @Override
  @Transactional(readOnly = true)
  public ImagesGetResponse getImageList(final long limjangId) {
    Limjang limjang = getLimjang(limjangId);
    List<Image> images = imageRepository.findImagesByLimjangId(limjang);
    return ImagesGetResponse.of(images);
  }

  private Limjang getLimjang(final long limjangId) {
    return limjangRepository.findByLimjangIdAndDeletedIsFalse(limjangId).orElseThrow(() -> new LimjangHandler(ErrorStatus.LIMJANG_NOTFOUND_ERROR));
  }
}
