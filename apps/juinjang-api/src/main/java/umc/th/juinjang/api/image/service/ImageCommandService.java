package umc.th.juinjang.api.image.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import umc.th.juinjang.api.image.controller.request.ImageDeleteRequestDTO;

public interface ImageCommandService {
    void createImages(long limjangId, List<MultipartFile> images);

    void deleteImages(ImageDeleteRequestDTO.DeleteDto ids);

}
