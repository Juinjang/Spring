package umc.th.juinjang.api.record.service.response;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


public class RecordResponseDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecordDTO{
        private String recordName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String recordScript;
        private Long recordTime;
        private String recordUrl;
        private Long recordId;
        private Long limjangId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecordMemoDto{
        private long limjangId;
        private String memo;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<RecordDTO> recordDto;
    }

}
