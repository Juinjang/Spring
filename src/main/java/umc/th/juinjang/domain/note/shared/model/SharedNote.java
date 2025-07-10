package umc.th.juinjang.domain.note.shared.model;

import java.sql.Timestamp;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.th.juinjang.api.note.shared.controller.request.SharedNotePostRequest;
import umc.th.juinjang.domain.common.BaseEntity;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.member.model.Member;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@DynamicUpdate
public class SharedNote extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sharedNoteId;

	private Long viewCount;

	private Timestamp deletedAt;

	private String buildingName;

	private boolean isImageShared;

	@Comment("임장시기 연도")
	@Column(name = "note_year")
	private Integer year;

	@Comment("임장시기 월")
	@Column(name = "note_month")
	private Integer month;

	// TODO : 임장시기 - 시기 추후에, ENUM 으로 변경 필요
	private String period;

	private String review;

	@Comment("임장 가격")
	private Long price;

	private Long likeCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "limjang_id", nullable = false)
	private Limjang limjang;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	public Long increaseLikedCount() {
		return this.likeCount = (likeCount == null ? 1L : likeCount + 1);
	}

	public static SharedNote toSharedNote(Member member, Limjang limjang, SharedNotePostRequest dto, Long price) {
		return SharedNote.builder()
			.member(member)
			.limjang(limjang)
			.buildingName(dto.buildingName())
			.review(dto.review())
			.year(dto.year())
			.month(dto.month())
			.period(dto.period())
			.isImageShared(dto.isImageShared())
			.viewCount(0L)
			.likeCount(0L)
			.price(price)
			.build();
	}

	public void updatePrice(long price) {
		this.price = price;
	}

	public void updateDeletedAt(Timestamp deletedAt) {
		this.deletedAt = deletedAt;
	}

	// 23년 12월 초반 임장
	public String getPullPeriod() {
		String shortYear = String.valueOf(this.year).substring(2);
		return shortYear + "년 " + this.month + "월 " + this.period + " 임장";
	}
}

