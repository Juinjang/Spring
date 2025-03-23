package umc.th.juinjang.domain.limjang.model;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.th.juinjang.domain.common.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Address extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long addressId;

	@Comment("기존 임장 테이블의 address")
	private String roadAddress;

	private String addressDetail;

	@Comment("법정동 코드")
	private String bcode;

	private String sido;

	private String sigungo;

	@Comment("읍/면")
	private String bname1;

	@Comment("동")
	private String bname2;
}
