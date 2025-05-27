package umc.th.juinjang.domain.pencil.acquired.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredPencil;

public interface AcquiredPencilRepository extends JpaRepository<AcquiredPencil, Long> {
	List<AcquiredPencil> findAllByMemberOrderByCreatedAtDesc(Member member);

	boolean existsByMember(Member member);
}
