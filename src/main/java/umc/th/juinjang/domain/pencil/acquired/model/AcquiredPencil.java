package umc.th.juinjang.domain.pencil.acquired.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.th.juinjang.domain.member.model.Member;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AcquiredPencil {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long acquiredPencilId;

	@ManyToOne
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	private String content;

	private Long sharedNoteId;

	private Long acquiredQuantity;

	private boolean isRead;

	@Enumerated(EnumType.STRING)
	private AcquiredType type; // Note, Add, Sold

	private LocalDateTime createdAt;

	@Builder
	private AcquiredPencil(Member member, String content, Long sharedNoteId, Long acquiredQuantity, boolean isRead,
		AcquiredType type, LocalDateTime createdAt) {
		this.member = member;
		this.content = content;
		this.sharedNoteId = sharedNoteId;
		this.acquiredQuantity = acquiredQuantity;
		this.isRead = isRead;
		this.type = type;
		this.createdAt = createdAt;
	}

	public static AcquiredPencil create(Member member, String content, Long sharedNoteId, Long acquiredQuantity,
		boolean isRead, AcquiredType type) {
		return AcquiredPencil.builder()
			.member(member)
			.content(content)
			.sharedNoteId(sharedNoteId)
			.acquiredQuantity(acquiredQuantity)
			.isRead(isRead)
			.type(type)
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static AcquiredPencil createWithDate(Member member, String content, Long sharedNoteId, Long acquiredQuantity,
		boolean isRead, AcquiredType type, LocalDateTime createdAt) {
		return AcquiredPencil.builder()
			.member(member)
			.content(content)
			.sharedNoteId(sharedNoteId)
			.acquiredQuantity(acquiredQuantity)
			.isRead(isRead)
			.type(type)
			.createdAt(createdAt)
			.build();
	}
}
