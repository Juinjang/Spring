package umc.th.juinjang.api.image.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import umc.th.juinjang.api.dto.ApiResponse;
import umc.th.juinjang.common.code.status.SuccessStatus;
import umc.th.juinjang.api.image.controller.request.ImageDeleteRequestDTO;
import umc.th.juinjang.api.image.service.response.ImagesGetResponse;
import umc.th.juinjang.api.image.service.ImageCommandService;
import umc.th.juinjang.api.image.service.ImageQueryService;

@RestController
@RequestMapping("/api/limjang/image")
@RequiredArgsConstructor
@Validated
public class ImageController {

  private final ImageCommandService imageCommandService;
  private final ImageQueryService imageQueryService;

  @Operation(summary = "사진 생성 API", description = "사진 업로드 api입니다.")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse uploadImages(@RequestParam(name = "limjangId") Long limjangId, @RequestPart(name = "images") List<MultipartFile> images) {
    imageCommandService.createImages(limjangId, images);
    return ApiResponse.onSuccess(SuccessStatus.IMAGE_UPDATE);
  }

  @Operation(summary = "사진 조회 API", description = "사진을 조회하는 api입니다.")
  @GetMapping(value = "/{limjangId}")
  public ApiResponse<ImagesGetResponse> uploadImages(@PathVariable(name = "limjangId") @Valid Long limjangId) {
    return ApiResponse.onSuccess(imageQueryService.getImageList(limjangId));
  }

  @Operation(summary = "이미지 선택 삭제", description = "이미지 게시글을 여러 개 선택해서 삭제하는 api입니다.")
  @PostMapping("/delete")
  public ApiResponse deleteImage(@RequestBody @Valid ImageDeleteRequestDTO.DeleteDto deleteIds) {
    imageCommandService.deleteImages(deleteIds);
    return ApiResponse.onSuccess(SuccessStatus.IMAGE_DELETE);
  }
}
