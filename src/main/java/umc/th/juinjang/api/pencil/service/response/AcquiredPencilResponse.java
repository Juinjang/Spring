package umc.th.juinjang.api.pencil.service.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredPencil;

@Getter
public class AcquiredPencilResponse {

	private Long acquiredPencilId;
	private String content;
	private Long sharedNoteId;
	private Long acquiredQuantity;
	private boolean isRead;
	private String type;
	private LocalDateTime createdAt;

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
			.acquiredPencilId(acquiredPencil.getAcquiredPencilId())
			.content(acquiredPencil.getContent())
			.sharedNoteId(acquiredPencil.getSharedNoteId())
			.acquiredQuantity(acquiredPencil.getAcquiredQuantity())
			.isRead(acquiredPencil.isRead())
			.type(acquiredPencil.getType().name())
			.createdAt(acquiredPencil.getCreatedAt())
			.build();
	}
}
