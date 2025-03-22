package umc.th.juinjang.api.limjang.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class LimjangMemoResponseDTO {


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemoDto{
        private long limjangId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String memo;
    }
}
