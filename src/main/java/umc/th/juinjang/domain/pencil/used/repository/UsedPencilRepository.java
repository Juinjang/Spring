package umc.th.juinjang.domain.pencil.used.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.pencil.used.model.UsedPencil;

@Repository
public interface UsedPencilRepository extends JpaRepository<UsedPencil, Long> {

	boolean existsByMemberAndSharedNoteId(Member member, Long sharedNoteId);

	List<UsedPencil> findAllByMemberOrderByCreatedAtDesc(Member member);
}
