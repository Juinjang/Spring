package umc.th.juinjang.api.checklist.service.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChecklistAnswerAndReportResponseDTO {
    private List<ChecklistAnswerResponseDTO.AnswerDto> answerDtoList;
    private ReportResponseDTO reportDto;

}
