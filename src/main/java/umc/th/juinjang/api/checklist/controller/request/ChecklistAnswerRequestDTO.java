package umc.th.juinjang.api.checklist.controller.request;

import lombok.*;


public class ChecklistAnswerRequestDTO {

    @Builder
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerDto {
        private Long questionId;
        private String answer;
    }
}
