package umc.th.juinjang.domain.pencil.acquired.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
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

	private int acquiredQuantity;

	private boolean isRead;

	@Enumerated(EnumType.STRING)
	private AcquiredType type; // Note, Add, Sold
}
