package umc.th.juinjang.domain.note.liked.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.note.shared.model.SharedNote;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class LikedNote {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long likedNoteId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shared_note_id", nullable = false)
	private SharedNote sharedNote;
	
}
