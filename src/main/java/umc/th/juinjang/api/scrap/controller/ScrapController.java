package umc.th.juinjang.api.scrap.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.th.juinjang.api.dto.ApiResponse;
import umc.th.juinjang.common.code.status.SuccessStatus;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.api.scrap.service.ScrapService;

@RestController
@RequestMapping("/api/limjangs/scraps")
@RequiredArgsConstructor
public class ScrapController {

  private final ScrapService scrapService;

  @Operation(summary = "스크랩 등록 API")
  @PostMapping("/{limjangId}")
  public ApiResponse addScrap (@AuthenticationPrincipal Member member, @PathVariable(name = "limjangId") long limjangId){
    scrapService.createScrap(member, limjangId);
    return ApiResponse.onSuccess(SuccessStatus._SCRAP_ACTION_SCRAP);
  }

  @Operation(summary = "스크랩 삭제 API")
  @DeleteMapping("/{limjangId}")
  public ApiResponse removeScrap (@AuthenticationPrincipal Member member, @PathVariable(name = "limjangId") long limjangId){
    scrapService.deleteScrap(member, limjangId);
    return ApiResponse.onSuccess(SuccessStatus._SCRAP_ACTION_UNSCRAP);
  }
}
