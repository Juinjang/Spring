package umc.th.juinjang.api.checklist.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.th.juinjang.domain.checklist.model.ChecklistQuestionType;

public class ChecklistAnswerResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerDto {
        private Long answerId;
        private Long questionId;
//        private ChecklistQuestionCategory category;
        private Long limjangId;
        private String answer;
        private ChecklistQuestionType answerType;
    }
}
