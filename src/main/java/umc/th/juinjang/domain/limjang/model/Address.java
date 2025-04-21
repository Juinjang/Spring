package umc.th.juinjang.domain.limjang.model;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Entity;
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

	@Builder
	private Address(String roadAddress, String addressDetail, String bcode, String sido, String sigungo,
		String bname1, String bname2) {
		this.roadAddress = roadAddress;
		this.addressDetail = addressDetail;
		this.bcode = bcode;
		this.sido = sido;
		this.sigungo = sigungo;
		this.bname1 = bname1;
		this.bname2 = bname2;
	}

	public static Address create(String roadAddress, String addressDetail, String bcode, String sido, String sigungo,
		String bname1, String bname2) {
		return Address.builder()
			.roadAddress(roadAddress)
			.addressDetail(addressDetail)
			.bcode(bcode)
			.sido(sido)
			.sigungo(sigungo)
			.bname1(bname1)
			.bname2(bname2)
			.build();
	}

	public String getShortAddress() {
		return Stream.of(sigungo, bname1, bname2)
			.filter(Objects::nonNull)
			.collect(Collectors.joining(" "));
	}

	public void update(Address newAddress) {
		this.roadAddress = newAddress.roadAddress;
		this.addressDetail = newAddress.addressDetail;
		this.bcode = newAddress.bcode;
		this.sido = newAddress.sido;
		this.sigungo = newAddress.sigungo;
		this.bname1 = newAddress.bname1;
		this.bname2 = newAddress.bname2;
	}

	public String getFullAddress() {
		return this.roadAddress + " " + this.getAddressDetail();
	}
}
