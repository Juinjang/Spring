package umc.th.juinjang.domain.limjang.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.th.juinjang.domain.checklist.model.ChecklistAnswer;
import umc.th.juinjang.domain.common.BaseEntity;
import umc.th.juinjang.domain.image.model.Image;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.record.model.Record;
import umc.th.juinjang.domain.report.model.Report;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
//@Where(clause = "deleted = false")
public class Limjang extends BaseEntity {

	@Id
	@Column(name = "limjang_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long limjangId;

	// 회원 ID
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member memberId;

	// 가격 ID
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "price_id", referencedColumnName = "price_id")
	private LimjangPrice limjangPrice;

	// 거래 목적
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private LimjangPurpose purpose;

	// 매물 유형
	@Enumerated(EnumType.STRING)
	private LimjangPropertyType propertyType;

	// 가격 유형
	@Enumerated(EnumType.STRING)
	private LimjangPriceType priceType;

	// 도로명 주소
	// @Column(nullable = false)
	private String address;

	private String addressDetail;

	// 보상 연필
	private int rewardPencil;

	// 집 별명
	@Column(nullable = false)
	private String nickname;

	@Column(columnDefinition = "text")
	private String memo;

	// 양방향 매핑
	@OneToMany(mappedBy = "limjangId", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ChecklistAnswer> answerList = new ArrayList<>();

	@OneToOne(mappedBy = "limjangId", cascade = CascadeType.ALL, orphanRemoval = true)
	private Report report;

	@OneToMany(mappedBy = "limjangId", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Record> recordList = new ArrayList<>();

	@OneToMany(mappedBy = "limjangId", cascade = CascadeType.ALL, orphanRemoval = true)
	@BatchSize(size = 100)
	private List<Image> imageList = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id")
	private Address addressEntity;

	@Column(name = "record_count")
	@ColumnDefault("0") //default 0
	private int recordCount;

	@Column(nullable = false, name = "deleted")
	private boolean deleted = Boolean.FALSE;

	public void saveMemberAndPrice(Member member, LimjangPrice limjangPrice) {
		this.limjangPrice = limjangPrice;
		this.memberId = member;
	}

	public void updateLimjang(String address, String addressDetail, String nickname, LimjangPriceType priceType) {
		this.address = address;
		this.addressDetail = addressDetail;
		this.nickname = nickname;
		this.priceType = priceType;
	}

	public void updateMemo(String memo) {
		this.memo = memo;
	}

	public void saveImages(Image image) {
		this.imageList.add(image);
	}

	public String getDefaultImage() {
		return this.imageList.isEmpty() ? null : this.imageList.get(0).getImageUrl();
	}

	@Builder
	private Limjang(Member member, LimjangPrice limjangPrice, LimjangPurpose purpose,
		LimjangPropertyType propertyType, LimjangPriceType priceType,
		int rewardPencil, String nickname, Address addressEntity) {
		this.memberId = member;
		this.limjangPrice = limjangPrice;
		this.purpose = purpose;
		this.propertyType = propertyType;
		this.priceType = priceType;
		this.rewardPencil = rewardPencil;
		this.nickname = nickname;
		this.addressEntity = addressEntity;
	}

	public static Limjang create(Member member, LimjangPrice price, LimjangPurpose purpose,
		LimjangPropertyType propertyType, LimjangPriceType priceType,
		int rewardPencil, String nickname, Address addressEntity) {
		return Limjang.builder()
			.memberId(member)
			.limjangPrice(price)
			.purpose(purpose)
			.propertyType(propertyType)
			.priceType(priceType)
			.rewardPencil(rewardPencil)
			.nickname(nickname)
			.addressEntity(addressEntity)
			.build();
	}
}
