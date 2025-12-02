package umc.th.juinjang.api.mock;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.checklist.service.response.ReportGetResponse;
import umc.th.juinjang.api.checklist.service.response.ReportWithLimjangResponseDTO;
import umc.th.juinjang.api.dto.ApiResponse;
import umc.th.juinjang.api.limjang.service.response.LimjangDetailGetResponse;
import umc.th.juinjang.api.limjang.service.response.LimjangsMainGetVersion2Response;
import umc.th.juinjang.api.limjang.service.response.UserNoteGetResponse;
import umc.th.juinjang.api.limjang.service.response.UserNotesGetResponse;
import umc.th.juinjang.api.record.service.response.RecordResponseDTO;

@RestController
@RequestMapping("/api/mock")
@RequiredArgsConstructor
public class MockController {

	@Operation(summary = "임장 메인화면에서 최근 임장 조회 Mock API")
	@GetMapping("/notes/recent")
	public ApiResponse<LimjangsMainGetVersion2Response> getRecentMockNotes() {
		return ApiResponse.onSuccess(LimjangsMainGetVersion2Response.mock());
	}

	@Operation(summary = "임장 상세 조회 Mock API")
	@GetMapping("/notes")
	public ApiResponse<UserNoteGetResponse> getMockNoteDetail() {
		return ApiResponse.onSuccess(UserNoteGetResponse.mock());
	}

	@Operation(summary = "마이노트 조회 Mock API")
	@GetMapping("/notes")
	public ApiResponse<UserNotesGetResponse> getMockUserNotes() {
		return ApiResponse.onSuccess(UserNotesGetResponse.mock());
	}

	@Operation(summary = "리포트 조회 Mock API")
	@GetMapping("/notes/mock/report")
	public ApiResponse<ReportWithLimjangResponseDTO> getMockReport() {
		ReportGetResponse reportMock = ReportGetResponse.mock();
		LimjangDetailGetResponse limjangMock = LimjangDetailGetResponse.mock();
		ReportWithLimjangResponseDTO response = new ReportWithLimjangResponseDTO(reportMock, limjangMock);
		return ApiResponse.onSuccess(response);
	}

	@Operation(summary = "녹음 및 메모 조회 Mock API")
	@GetMapping("/notes/mock/records")
	public ApiResponse<RecordResponseDTO.RecordMemoDto> getMockRecordMemo() {

		// Mock 메모 데이터 생성
		RecordResponseDTO.RecordMemoDto response = RecordResponseDTO.RecordMemoDto.builder()
			.limjangId(noteId)
			.memo("- 베란다 확장 공사 완료. 냉기 차단 잘 됨 - 단지내 어린이집")
			.createdAt(LocalDateTime.of(2024, 11, 15, 10, 30))
			.updatedAt(LocalDateTime.of(2024, 11, 20, 17, 0))
			.recordDto(List.of())
			.build();

		return ApiResponse.onSuccess(response);
	}

}
