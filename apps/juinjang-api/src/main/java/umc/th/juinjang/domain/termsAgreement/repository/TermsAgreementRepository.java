package umc.th.juinjang.domain.termsAgreement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermsAgreementRepository extends JpaRepository<TermsAgreement, Long> {
	// 멤버가 특정 약관에 동의 했는 여부를 체크하는 메서드
	Optional<TermsAgreement> findByMemberIdAndTermsType(Long memberId, TermsType termsType);
}
