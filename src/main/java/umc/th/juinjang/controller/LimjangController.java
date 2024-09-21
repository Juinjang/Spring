package umc.th.juinjang.controller;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import umc.th.juinjang.apiPayload.ApiResponse;
import umc.th.juinjang.apiPayload.code.status.SuccessStatus;
import umc.th.juinjang.converter.limjang.LimjangPostRequestConverter;
import umc.th.juinjang.model.dto.limjang.enums.LimjangSortOptions;
import umc.th.juinjang.model.dto.limjang.request.LimjangPostRequest;
import umc.th.juinjang.model.dto.limjang.request.LimjangUpdateRequestDTO;
import umc.th.juinjang.model.dto.limjang.request.LimjangsDeleteRequest;
import umc.th.juinjang.model.dto.limjang.response.LimjangDetailResponseDTO;
import umc.th.juinjang.model.dto.limjang.response.LimjangPostResponseDTO;
import umc.th.juinjang.model.dto.limjang.response.LimjangTotalListResponseDTO;
import umc.th.juinjang.model.dto.limjang.response.LimjangsGetResponse;
import umc.th.juinjang.model.dto.limjang.response.LimjangsMainGetResponse;
import umc.th.juinjang.model.entity.Member;
import umc.th.juinjang.service.LimjangService.LimjangCommandService;
import umc.th.juinjang.service.LimjangService.LimjangQueryService;

@RestController
@RequestMapping("/api/limjang")
@RequiredArgsConstructor
@Validated
public class LimjangController {

  private final LimjangCommandService limjangCommandService;
  private final LimjangQueryService limjangQueryService;

  @CrossOrigin
  @Operation(summary = "임장 생성 API")
  @PostMapping
  public ApiResponse<LimjangPostResponseDTO.PostDTO> postLimjang(@RequestBody @Valid LimjangPostRequest postDto, @AuthenticationPrincipal Member member){
    return ApiResponse.onSuccess(LimjangPostRequestConverter.toPostDTO(limjangCommandService.postLimjang(postDto, member)));
  }

  @CrossOrigin
  @Operation(summary = "임장 전체 조회 API")
  @GetMapping
  public ApiResponse<LimjangsGetResponse> getLimjangTotalList(@AuthenticationPrincipal Member member, @RequestParam("sort") LimjangSortOptions sort){
    return ApiResponse.onSuccess(limjangQueryService.getLimjangTotalList(member, sort));
  }

  @CrossOrigin
  @Operation(summary = "임장 메인화면에서 최근 임장 조회 API", description = "가장 최근에 수정된 순으로 최대 5개까지 볼 수 있다.")
  @GetMapping("/main")
  public ApiResponse<LimjangsMainGetResponse> getRecentUpdateList(
      @AuthenticationPrincipal Member member
  ){
    return ApiResponse.onSuccess((limjangQueryService.getLimjangsMain(member)));
  }

  @CrossOrigin
  @Operation(summary = "임장 선택 삭제", description = "임장 게시글을 여러 개 선택해서 삭제하는 api입니다.")
  @DeleteMapping
  public ApiResponse deleteLimjang(@RequestBody @Valid LimjangsDeleteRequest deleteIds, @AuthenticationPrincipal Member member){
    limjangCommandService.deleteLimjangs(deleteIds, member);
    return ApiResponse.onSuccess(SuccessStatus.LIMJANG_DELETE);
  }

  @CrossOrigin
  @Operation(summary = "임장 검색", description = "임장 게시글을 검색하는 api입니다. 집별명, 일반주소, 상세주소로 검색이 가능합니다.")
  @GetMapping("/{keyword}")
  public ApiResponse<LimjangTotalListResponseDTO.TotalListDto> searchLimjangs(
      @AuthenticationPrincipal Member member,
      @PathVariable(name = "keyword") @Valid String keyword
  ) {

      return ApiResponse.onSuccess(limjangQueryService.getLimjangSearchList(member, keyword));
  }

  @CrossOrigin
  @Operation(summary = "임장 상세보기", description = "임장 상세보기 api입니다. 임장 id를 전달해주세요.")
  @GetMapping("/detail/{limjangId}")
  public ApiResponse<LimjangDetailResponseDTO.DetailDto> getDetailLimjang(
      @PathVariable(name = "limjangId") @Valid Long limjangId,
      @AuthenticationPrincipal Member member
  ) {
    return ApiResponse.onSuccess(limjangQueryService.getLimjangDetail(limjangId));
  }

  @CrossOrigin
  @Operation(summary = "임장 기본정보 수정", description = "임장 기본정보수정 api입니다. 수정할 정보를 전달해주세요.")
  @PatchMapping("/{limjangId}")
  public ApiResponse updateLimjang(
      @AuthenticationPrincipal Member member,
      @PathVariable(name = "limjangId") @Valid long limjangId,
      @RequestBody @Valid
      LimjangUpdateRequestDTO.UpdateDto updateLimjang
  ) {
    limjangCommandService.updateLimjang(member.getMemberId(), limjangId, updateLimjang);
    return ApiResponse.onSuccess(SuccessStatus.LIMJANG_UPDATE);
  }
}
