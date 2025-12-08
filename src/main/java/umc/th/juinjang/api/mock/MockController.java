package umc.th.juinjang.api.mock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.checklist.service.response.ChecklistAnswerResponseDTO;
import umc.th.juinjang.api.checklist.service.response.ReportGetResponse;
import umc.th.juinjang.api.checklist.service.response.ReportWithLimjangResponseDTO;
import umc.th.juinjang.api.dto.ApiResponse;
import umc.th.juinjang.api.limjang.service.response.LimjangDetailGetResponse;
import umc.th.juinjang.api.limjang.service.response.LimjangsMainGetVersion2Response;
import umc.th.juinjang.api.limjang.service.response.UserNoteGetResponse;
import umc.th.juinjang.api.limjang.service.response.UserNotesGetResponse;
import umc.th.juinjang.api.record.service.response.RecordResponseDTO;
import umc.th.juinjang.domain.checklist.model.ChecklistQuestionCategory;
import umc.th.juinjang.domain.checklist.model.ChecklistQuestionType;

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
	@GetMapping("/notes/report")
	public ApiResponse<ReportWithLimjangResponseDTO> getMockReport() {
		ReportGetResponse reportMock = ReportGetResponse.mock();
		LimjangDetailGetResponse limjangMock = LimjangDetailGetResponse.mock();
		ReportWithLimjangResponseDTO response = new ReportWithLimjangResponseDTO(reportMock, limjangMock);
		return ApiResponse.onSuccess(response);
	}

	@Operation(summary = "녹음 및 메모 조회 Mock API")
	@GetMapping("/notes/records")
	public ApiResponse<RecordResponseDTO.RecordMemoDto> getMockRecordMemo() {

		// Mock 메모 데이터 생성
		RecordResponseDTO.RecordMemoDto response = RecordResponseDTO.RecordMemoDto.builder()
			.limjangId(1L)
			.memo("- 베란다 확장 공사 완료. 냉기 차단 잘 됨 - 단지내 어린이집")
			.createdAt(LocalDateTime.of(2024, 11, 15, 10, 30))
			.updatedAt(LocalDateTime.of(2024, 11, 20, 17, 0))
			.recordDto(List.of())
			.build();

		return ApiResponse.onSuccess(response);
	}

	@Operation(summary = "체크리스트 조회 Mock API")
	@GetMapping("/notes/checklists")
	public ApiResponse<List<ChecklistAnswerResponseDTO.AnswerDto>> getMockChecklistAnswer() {
		Long limjangId = 1L;

		// 카테고리, 답변, 답변타입 순서로 정의
		Object[][] mockData = {
			// 입지여건
			{ChecklistQuestionCategory.LOCATION_CONDITION, "5", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.LOCATION_CONDITION, "2호선 신당역", ChecklistQuestionType.DROPDOWN},
			{ChecklistQuestionCategory.LOCATION_CONDITION, "3", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.LOCATION_CONDITION, "4", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.LOCATION_CONDITION, "5", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.LOCATION_CONDITION, "5", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.LOCATION_CONDITION, "5", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.LOCATION_CONDITION, "5", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.LOCATION_CONDITION, "1", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.LOCATION_CONDITION, "아파트 단지", ChecklistQuestionType.DROPDOWN},
			{ChecklistQuestionCategory.LOCATION_CONDITION, "4", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.LOCATION_CONDITION, "남향", ChecklistQuestionType.DROPDOWN},
			{ChecklistQuestionCategory.LOCATION_CONDITION, "4", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.LOCATION_CONDITION, "5", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.LOCATION_CONDITION, "5", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.LOCATION_CONDITION, "1", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.LOCATION_CONDITION, "5", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.LOCATION_CONDITION, "2000년도", ChecklistQuestionType.TEXT_FIELD},
			{ChecklistQuestionCategory.LOCATION_CONDITION, "3", ChecklistQuestionType.SCORE},
			// 공용공간
			{ChecklistQuestionCategory.PUBLIC_SPACE, "2대", ChecklistQuestionType.TEXT_FIELD},
			{ChecklistQuestionCategory.PUBLIC_SPACE, "4", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.PUBLIC_SPACE, "4", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.PUBLIC_SPACE, "1", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.PUBLIC_SPACE, "3", ChecklistQuestionType.SCORE},
			// 실내
			{ChecklistQuestionCategory.INDOOR, "설치형에어컨", ChecklistQuestionType.DROPDOWN},
			{ChecklistQuestionCategory.INDOOR, "2", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.INDOOR, "5", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.INDOOR, "거실중앙형 구조", ChecklistQuestionType.DROPDOWN},
			{ChecklistQuestionCategory.INDOOR, "5", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.INDOOR, "3", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.INDOOR, "4", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.INDOOR, "1", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.INDOOR, "3", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.INDOOR, "4", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.INDOOR, "3", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.INDOOR, "4", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.INDOOR, "3", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.INDOOR, "4", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.INDOOR, "5", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.INDOOR, "5", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.INDOOR, "4", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.INDOOR, "2", ChecklistQuestionType.TEXT_FIELD},
			{ChecklistQuestionCategory.INDOOR, "4", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.INDOOR, "4", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.INDOOR, "5", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.INDOOR, "4", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.INDOOR, "3", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.INDOOR, "3", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.INDOOR, "5", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.INDOOR, "3", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.INDOOR, "5", ChecklistQuestionType.SCORE},
			{ChecklistQuestionCategory.INDOOR, "5", ChecklistQuestionType.SCORE}
		};

		List<ChecklistAnswerResponseDTO.AnswerDto> answers = new ArrayList<>();
		for (int i = 0; i < mockData.length; i++) {
			answers.add(ChecklistAnswerResponseDTO.AnswerDto.builder()
				.answerId((long)(i + 1))
				.questionId((long)(i + 1))
				.category((ChecklistQuestionCategory)mockData[i][0])
				.limjangId(limjangId)
				.answer((String)mockData[i][1])
				.answerType((ChecklistQuestionType)mockData[i][2])
				.build());
		}

		return ApiResponse.onSuccess(answers);
	}

}
