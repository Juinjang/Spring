package umc.th.juinjang.api.checklist.service.converter;


public class ChecklistQuestionConverter {
//    public static List<ChecklistQuestionDTO.QuestionDto> toChecklistQuestionListDTO(List<ChecklistQuestionShort> checklistQuestionShorts) {
//        return checklistQuestionShorts.stream()
//                .map(ChecklistQuestionConverter::questionDto).collect(Collectors.toList());
//    }
//    public static List<ChecklistQuestionDTO.OptionDto> toChecklistAnswerOptionDTO(List<ChecklistAnswerOption> checklistAnswerOptions) {
//        return checklistAnswerOptions.stream()
//                .map(ChecklistQuestionConverter::optionDto).collect(Collectors.toList());
//    }

//    public static ChecklistQuestionDTO.OptionDto optionDto(ChecklistAnswerOption checklistAnswerOption) {
//        return ChecklistQuestionDTO.OptionDto.builder()
//                .indexNum(checklistAnswerOption.getIndexNum())
//                .questionId(checklistAnswerOption.getQuestionId().getQuestionId())
//                .optionValue(checklistAnswerOption.getOptionValue())
//                .build();
//    }
//
//    public static ChecklistQuestionDTO.QuestionDto questionDto(ChecklistQuestionShort checklistQuestionShort) {
//        return ChecklistQuestionDTO.QuestionDto.builder()
//                .questionId(checklistQuestionShort.getQuestionId())
//                .category(checklistQuestionShort.getCategory().getValue())
//                .subCategory(checklistQuestionShort.getSubCategory())
//                .question(checklistQuestionShort.getQuestion())
//                .version(checklistQuestionShort.getPurpose().getValue())
//                .answerType(checklistQuestionShort.getAnswerType().getValue())
//                .options(ChecklistQuestionConverter.toChecklistAnswerOptionDTO(checklistQuestionShort.getAnswerOptions()))
//                .build();
//    }

}
