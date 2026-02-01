package umc.th.juinjang.api.checklist.service.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.th.juinjang.domain.checklist.model.ChecklistAnswer;
import umc.th.juinjang.domain.checklist.model.ChecklistQuestionCategory;
import umc.th.juinjang.domain.checklist.model.ChecklistQuestionType;

public class ChecklistAnswerResponseDTO {
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AnswerDto {
		private Long answerId;
		private Long questionId;
		private ChecklistQuestionCategory category;
		private Long limjangId;
		private String answer;
		private ChecklistQuestionType answerType;

		public static AnswerDto fromEntity(ChecklistAnswer entity) {
			return AnswerDto.builder()
				.answerId(entity.getAnswerId())
				.questionId(entity.getQuestionId().getQuestionId())
				.category(entity.getQuestionId().getCategory())
				.limjangId(entity.getLimjangId().getLimjangId())
				.answer(entity.getAnswer())
				.answerType(entity.getQuestionId().getAnswerType())
				.build();
		}

		public static List<AnswerDto> fromEntityList(List<ChecklistAnswer> entities) {
			return entities.stream()
				.map(AnswerDto::fromEntity)
				.toList(); // Java 17+ 지원
		}
	}
}
