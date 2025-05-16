package umc.th.juinjang.domain.flag.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.th.juinjang.domain.common.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FlagSharedNote extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sharedNoteFlagId;

	@Enumerated(EnumType.STRING)
	private FlagSharedNoteType type;

	@Enumerated(EnumType.STRING)
	private FlagSharedNoteStatus status;

	private Long flagged_by_member_id;

	private Long sharedNoteId;

	@Builder
	private FlagSharedNote(FlagSharedNoteType type, FlagSharedNoteStatus status, Long flagged_by_member_id,
		Long sharedNoteId) {
		this.type = type;
		this.status = status;
		this.flagged_by_member_id = flagged_by_member_id;
		this.sharedNoteId = sharedNoteId;
	}

	public static FlagSharedNote create(FlagSharedNoteType type, Long flagged_by_member_id,
		Long sharedNoteId) {
		return FlagSharedNote.builder()
			.type(type)
			.status(FlagSharedNoteStatus.RECEIVED)
			.flagged_by_member_id(flagged_by_member_id)
			.sharedNoteId(sharedNoteId)
			.build();
	}
}
