package umc.th.juinjang.api.note.shared.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SharedNotePostRequest(
	@NotBlank String buildingName,
	@NotBlank String review,
	@NotNull Boolean isImageShared,
	@NotNull Integer year,
	@NotNull Integer month,
	@NotBlank String period,
	@NotNull Long price
) {
}
