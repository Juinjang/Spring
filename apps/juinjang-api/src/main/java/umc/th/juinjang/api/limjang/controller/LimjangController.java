package umc.th.juinjang.api.limjang.controller;


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
import umc.th.juinjang.api.dto.ApiResponse;
import umc.th.juinjang.common.code.status.SuccessStatus;
import umc.th.juinjang.api.limjang.controller.parameter.LimjangSortOptions;
import umc.th.juinjang.api.limjang.controller.request.LimjangPatchRequest;
import umc.th.juinjang.api.limjang.controller.request.LimjangPostRequest;
import umc.th.juinjang.api.limjang.controller.request.LimjangsDeleteRequest;
import umc.th.juinjang.api.limjang.service.response.LimjangDetailGetResponse;
import umc.th.juinjang.api.limjang.service.response.LimjangPostResponse;
import umc.th.juinjang.api.limjang.service.response.LimjangsGetByKeywordResponse;
import umc.th.juinjang.api.limjang.service.response.LimjangsGetResponse;
import umc.th.juinjang.api.limjang.service.response.LimjangsMainGetResponse;
import umc.th.juinjang.api.limjang.service.response.LimjangsMainGetVersion2Response;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.api.limjang.service.LimjangCommandService;
import umc.th.juinjang.api.limjang.service.LimjangQueryService;

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
  public ApiResponse<LimjangPostResponse> postLimjang(@RequestBody @Valid LimjangPostRequest postDto, @AuthenticationPrincipal Member member){
    return ApiResponse.onSuccess(LimjangPostResponse.of(limjangCommandService.postLimjang(postDto, member)));
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
  public ApiResponse<LimjangsMainGetResponse> getRecentUpdateList(@AuthenticationPrincipal Member member) {
    return ApiResponse.onSuccess((limjangQueryService.getLimjangsMain(member)));
  }

  @CrossOrigin
  @Operation(summary = "임장 메인화면에서 최근 임장 조회 API version 2", description = "가장 최근에 수정된 순으로 최대 5개까지 볼 수 있다.")
  @GetMapping("/v2/main")
  public ApiResponse<LimjangsMainGetVersion2Response> getRecentUpdateListVersion2(@AuthenticationPrincipal Member member) {
    return ApiResponse.onSuccess((limjangQueryService.getLimjangsMainVersion2(member)));
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
  public ApiResponse<LimjangsGetByKeywordResponse> searchLimjangs(@AuthenticationPrincipal Member member, @PathVariable(name = "keyword") String keyword) {
      return ApiResponse.onSuccess(limjangQueryService.getLimjangSearchList(member, keyword));
  }

  @CrossOrigin
  @Operation(summary = "임장 상세보기", description = "임장 상세보기 api입니다. 임장 id를 전달해주세요.")
  @GetMapping("/detail/{limjangId}")
  public ApiResponse<LimjangDetailGetResponse> getLimjang(@PathVariable(name = "limjangId") @Valid Long id, @AuthenticationPrincipal Member member) {
    return ApiResponse.onSuccess(limjangQueryService.getDetail(id, member));
  }

  @CrossOrigin
  @Operation(summary = "임장 기본정보 수정", description = "임장 기본 정보 수정 api입니다. 수정할 정보를 전달해주세요.")
  @PatchMapping("/{limjangId}")
  public ApiResponse updateLimjang(@AuthenticationPrincipal Member member, @PathVariable(name = "limjangId") long id, @RequestBody @Valid LimjangPatchRequest request) {
    limjangCommandService.updateLimjang(member, id, request);
    return ApiResponse.onSuccess(SuccessStatus.LIMJANG_UPDATE);
  }
}
