package umc.th.juinjang.domain.pencil.acquired.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredPencil;

@Repository
public interface AcquiredPencilRepository extends JpaRepository<AcquiredPencil, Long> {

	List<AcquiredPencil> findAllByMember(Member member);
}
