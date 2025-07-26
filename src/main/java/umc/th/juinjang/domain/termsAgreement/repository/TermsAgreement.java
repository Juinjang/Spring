package umc.th.juinjang.domain.termsAgreement.repository;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TermsAgreement {

	@Id
	@Column(name = "terms_agreement_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long memberId;

	@Enumerated(EnumType.STRING)
	private TermsType termsType;

	private LocalDateTime agreedAt;

	public static TermsAgreement create(Long memberId, TermsType termsType) {
		return TermsAgreement.builder()
			.memberId(memberId)
			.termsType(termsType)
			.agreedAt(LocalDateTime.now())
			.build();
	}
}
