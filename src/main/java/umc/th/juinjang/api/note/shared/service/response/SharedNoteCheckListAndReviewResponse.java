package umc.th.juinjang.api.note.shared.service.response;

import java.util.List;

import umc.th.juinjang.api.checklist.service.response.ChecklistAnswerResponseDTO;

public record SharedNoteCheckListAndReviewResponse(
	String review,
	List<ChecklistAnswerResponseDTO.AnswerDto> checklistAnswers
) {
}