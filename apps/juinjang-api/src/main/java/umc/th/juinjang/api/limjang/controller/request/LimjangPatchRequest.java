package umc.th.juinjang.api.limjang.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public record LimjangPatchRequest(
    int priceType,
    @Size(min = 1, max = 2)
    List<String> priceList,
    @NotBlank
    String address,
    String addressDetail,
    @NotBlank
    String nickname
) {
}