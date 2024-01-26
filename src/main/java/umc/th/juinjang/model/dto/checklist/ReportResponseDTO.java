package umc.th.juinjang.model.dto.checklist;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
public class ReportResponseDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReportDTO {
        private Long reportId;
        private String indoorKeyWord;
        private String publicSpaceKeyWord;
        private String locationConditionsWord;
        private Float indoorRate;
        private Float publicSpaceRate;
        private Float locationConditionsRate;
        private Float totalRate;
    }
}