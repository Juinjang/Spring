package umc.th.juinjang.domain.pencil.used.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.th.juinjang.domain.common.BaseEntity;
import umc.th.juinjang.domain.member.model.Member;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
	name = "used_pencil",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"member_id", "shared_note_id"})
	}
)
public class UsedPencil extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long usedPencilId;

	@ManyToOne
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(name = "shared_note_id")
	private Long sharedNoteId;

	private int usedQuantity;

	// TODO: 우선 OWNED(소장) 하나만 추가
	@Enumerated(EnumType.STRING)
	private Usedtype type;

	private String buildingName;

	private Long remainQuantity;

	public static UsedPencil create(Member member, Long sharedNoteId, int usedQuantity, Usedtype type,
		String buildingName, Long remainQuantity) {
		return new UsedPencil(
			member,
			sharedNoteId,
			usedQuantity,
			type,
			buildingName,
			remainQuantity
		);
	}

	@Builder
	private UsedPencil(Member member, Long sharedNoteId, int usedQuantity, Usedtype type,
		String buildingName, Long remainQuantity) {
		this.member = member;
		this.sharedNoteId = sharedNoteId;
		this.usedQuantity = usedQuantity;
		this.type = type;
		this.buildingName = buildingName;
		this.remainQuantity = remainQuantity;
	}

}
