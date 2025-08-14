package umc.th.juinjang.api.pencil.service.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredType;

@Getter
public class AcquiredPencilResponse {

	private final Long acquiredPencilId;
	private final String content;
	private final Long sharedNoteId;
	private final Long acquiredQuantity;
	private final String buildingName;
	private final boolean isRead;
	private final String type;
	private final LocalDateTime createdAt;

	@Builder
	public AcquiredPencilResponse(Long acquiredPencilId, String content, Long sharedNoteId, Long acquiredQuantity,
		String buildingName, boolean isRead, String type, LocalDateTime createdAt) {
		this.acquiredPencilId = acquiredPencilId;
		this.content = content;
		this.sharedNoteId = sharedNoteId;
		this.acquiredQuantity = acquiredQuantity;
		this.buildingName = buildingName;
		this.isRead = isRead;
		this.type = type;
		this.createdAt = createdAt;
	}

	public AcquiredPencilResponse(Long acquiredPencilId, String content, Long sharedNoteId,
		Long acquiredQuantity, boolean isRead, AcquiredType type,
		LocalDateTime createdAt, String buildingName) {
		this.acquiredPencilId = acquiredPencilId;
		this.content = content;
		this.sharedNoteId = sharedNoteId;
		this.acquiredQuantity = acquiredQuantity;
		this.isRead = isRead;
		this.type = type.name();
		this.createdAt = createdAt;
		this.buildingName = buildingName;
	}
}
