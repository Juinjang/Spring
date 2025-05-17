package umc.th.juinjang.api.pencil.service.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import umc.th.juinjang.domain.pencil.used.model.UsedPencil;
import umc.th.juinjang.domain.pencil.used.model.Usedtype;

@Getter
public class UsedPencilResponse {

	private Long usedPencilId;
	private Long useQuantity;
	private Usedtype type;
	private Long remainQuantity;
	private String buildingName;
	private Long sharedNoteId;
	private LocalDateTime createdAt;

	@Builder
	public UsedPencilResponse(Long usedPencilId, Long useQuantity, Usedtype type,
		Long remainQuantity, String buildingName,
		Long sharedNoteId, LocalDateTime createdAt) {
		this.usedPencilId = usedPencilId;
		this.useQuantity = useQuantity;
		this.type = type;
		this.remainQuantity = remainQuantity;
		this.buildingName = buildingName;
		this.sharedNoteId = sharedNoteId;
		this.createdAt = createdAt;
	}

	public static UsedPencilResponse from(UsedPencil usedPencil) {
		return UsedPencilResponse.builder()
			.usedPencilId(usedPencil.getUsedPencilId())
			.useQuantity(usedPencil.getUsedQuantity())
			.type(usedPencil.getType())
			.remainQuantity(usedPencil.getRemainQuantity())
			.buildingName(usedPencil.getBuildingName())
			.sharedNoteId(usedPencil.getSharedNoteId())
			.createdAt(usedPencil.getCreatedAt())
			.build();
	}
}
