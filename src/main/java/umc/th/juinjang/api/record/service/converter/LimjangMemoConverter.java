package umc.th.juinjang.api.record.service.converter;

import umc.th.juinjang.api.limjang.service.response.LimjangMemoResponseDTO;
import umc.th.juinjang.domain.limjang.model.Limjang;

public class LimjangMemoConverter {

    public static LimjangMemoResponseDTO.MemoDto toDto(Limjang limjang){
        return LimjangMemoResponseDTO.MemoDto.builder()
                .memo(limjang.getMemo())
                .limjangId(limjang.getLimjangId())
                .createdAt(limjang.getCreatedAt())
                .updatedAt(limjang.getUpdatedAt())
                .build();
    }
}
