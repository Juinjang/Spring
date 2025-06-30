package umc.th.juinjang.api.pencil.service.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredPencil;

@Getter
public class AcquiredPencilResponse {

	private final Long acquiredPencilId;
	private final String content;
	private final Long sharedNoteId;
	private final Long acquiredQuantity;
	private final boolean isRead;
	private final String type;
	private final LocalDateTime createdAt;

	@Builder
	public AcquiredPencilResponse(Long acquiredPencilId, String content, Long sharedNoteId, Long acquiredQuantity,
		boolean isRead, String type, LocalDateTime createdAt) {
		this.acquiredPencilId = acquiredPencilId;
		this.content = content;
		this.sharedNoteId = sharedNoteId;
		this.acquiredQuantity = acquiredQuantity;
		this.isRead = isRead;
		this.type = type;
		this.createdAt = createdAt;
	}

	public static AcquiredPencilResponse from(AcquiredPencil acquiredPencil) {
		return AcquiredPencilResponse.builder()
			.acquiredPencilId(acquiredPencil.getId())
			.content(acquiredPencil.getContent())
			.sharedNoteId(acquiredPencil.getSharedNoteId())
			.acquiredQuantity(acquiredPencil.getAcquiredQuantity())
			.isRead(acquiredPencil.isRead())
			.type(acquiredPencil.getType().name())
			.createdAt(acquiredPencil.getCreatedAt())
			.build();
	}
}
