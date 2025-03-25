package umc.th.juinjang.domain.pencil.used.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.th.juinjang.domain.common.BaseEntity;
import umc.th.juinjang.domain.member.model.Member;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UsedPencil extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long usedPencilId;

	@ManyToOne
	@JoinColumn(name = "member_id", nullable = false)
	private Member memberId;

	private Long usedQuantity;

	// TODO: 우선 OWNED(소장) 하나만 추가
	private String type;

	private String title;

	private Long remainQuantity;
}
