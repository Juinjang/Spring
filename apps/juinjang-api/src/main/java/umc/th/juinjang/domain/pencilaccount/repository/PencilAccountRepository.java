package umc.th.juinjang.domain.pencilaccount.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.pencilaccount.model.PencilAccount;

@Repository
public interface PencilAccountRepository extends JpaRepository<PencilAccount, Long> {

	Optional<PencilAccount> findByMember(Member member);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM PencilAccount p WHERE p.member = :member")
	Optional<PencilAccount> findByMemberWithLock(@Param("member") Member member);
}
