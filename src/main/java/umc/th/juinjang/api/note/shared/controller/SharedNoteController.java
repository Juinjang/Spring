package umc.th.juinjang.api.note.shared.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.annotations.PropertyType;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.dto.ApiResponse;
import umc.th.juinjang.api.note.shared.service.SharedNoteCommandService;
import umc.th.juinjang.api.note.shared.service.SharedNoteQueryService;
import umc.th.juinjang.api.note.shared.service.response.SharedNoteExploreGetResponse;
import umc.th.juinjang.api.note.shared.service.response.SharedNoteGetResponse;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.member.model.Member;

@RestController
@RequestMapping("/api/v2/shared-notes")
@RequiredArgsConstructor
public class SharedNoteController {

	private final SharedNoteCommandService sharedNoteCommandService;
	private final SharedNoteQueryService sharedNoteQueryService;

	@Operation(summary = "노트 구매 API")
	@PostMapping("/{sharedNoteId}/purchase")
	public ApiResponse<Void> createSharedNotePurchase(@AuthenticationPrincipal Member member,
		@PathVariable("sharedNoteId") Long sharedNoteId) {
		sharedNoteCommandService.createSharedNotePurchase(member, sharedNoteId);
		return ApiResponse.onSuccess(null);
	}

	@Operation(summary = "공유 노트 상세보기 API")
	@GetMapping("/{sharedNoteId}")
	public ApiResponse<SharedNoteGetResponse> findSharedNote(@AuthenticationPrincipal Member member,
		@PathVariable("sharedNoteId") Long sharedNoteId) {
		return ApiResponse.onSuccess(sharedNoteQueryService.findSharedNote(member, sharedNoteId));
	}

	@Operation(summary = "공유 노트 둘러보기 API")
	@GetMapping("/explore")
	// code={지역코드}&limit=10&cursor={마지막sharednoteId}&sort={정렬조건}&propertyType={매물유형}&priceType={가격유형}&keyword={검색어}
	public ApiResponse<SharedNoteExploreGetResponse> findSharedNote(@AuthenticationPrincipal Member member,
		@RequestParam(value = "code", required = false) List<String> code,
		@RequestParam(value = "sort", required = false) ExploreSortType sort,
		@RequestParam(value = "propertyType", required = false) LimjangPropertyType propertyType,
		@RequestParam(value = "priceType", required = false) LimjangPriceType priceType,
		@RequestParam(value = "keyword", required = false) String keyword,
		Pageable pageable
	) {
		return ApiResponse.onSuccess(
			sharedNoteQueryService.findExploreSharedNote(member, code, sort, propertyType, priceType,
				keyword, pageable));
	}
}
