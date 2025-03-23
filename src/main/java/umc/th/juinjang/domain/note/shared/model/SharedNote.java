package umc.th.juinjang.domain.note.shared.model;

import java.sql.Timestamp;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.th.juinjang.domain.common.BaseEntity;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.member.model.Member;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SharedNote extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sharedNoteId;

	private Long viewCount;

	private Timestamp deletedAt;

	private String buildingName;

	private boolean isImageShared;

	@Comment("임장시기 연도")
	private int year;

	@Comment("임장시기 월")
	private int month;

	// TODO : 임장시기 - 시기 추후에, ENUM 으로 변경 필요
	private String period;

	private String review;

	@Comment("임장 가격")
	private int price;

	@OneToOne
	@JoinColumn(name = "limjang_id", nullable = false, unique = true)
	private Limjang limjang;

	@ManyToOne
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;
}

