package umc.th.juinjang.api.checklist.service.converter;

import umc.th.juinjang.api.checklist.service.response.ChecklistAnswerResponseDTO;
import umc.th.juinjang.domain.checklist.model.ChecklistAnswer;

import java.util.List;
import java.util.stream.Collectors;

public class ChecklistAnswerConverter {

    public static ChecklistAnswerResponseDTO.AnswerDto toDto(ChecklistAnswer entity) {
        return ChecklistAnswerResponseDTO.AnswerDto.builder()
                .answerId(entity.getAnswerId())
                .questionId(entity.getQuestionId().getQuestionId())
                .limjangId(entity.getLimjangId().getLimjangId())
                .answer(entity.getAnswer())
                .build();
    }

    public static List<ChecklistAnswerResponseDTO.AnswerDto> toDtoList(List<ChecklistAnswer> entityList) {
        return entityList.stream()
                .map(ChecklistAnswerConverter::toDto)
                .collect(Collectors.toList());
    }
}
