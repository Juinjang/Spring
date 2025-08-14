package umc.th.juinjang.api.checklist.controller.request;

import lombok.*;

import java.util.List;


public class ChecklistQuestionDTO {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionListDto {
        private Integer category;
        private List<QuestionDto> questionDtos;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionDto {
        private Long questionId;
        private Integer category;
        private String subCategory;
        private String question;
        private Integer version;
        private Integer answerType;
        private List<OptionDto> options;
        private String answer;
    }



    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionDto {
        private Integer indexNum;
        private Long questionId;
        private String optionValue;
    }
}
