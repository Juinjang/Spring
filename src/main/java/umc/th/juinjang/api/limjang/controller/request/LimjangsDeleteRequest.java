package umc.th.juinjang.api.limjang.controller.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record LimjangsDeleteRequest(
    @NotEmpty List<Long> limjangIdList
) {
}