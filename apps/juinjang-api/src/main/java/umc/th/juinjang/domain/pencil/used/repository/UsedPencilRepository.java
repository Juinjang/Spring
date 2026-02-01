package umc.th.juinjang.domain.pencil.used.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.pencil.used.model.UsedPencil;

@Repository
public interface UsedPencilRepository extends JpaRepository<UsedPencil, Long> {

	boolean existsByMemberAndSharedNoteId(Member member, Long sharedNoteId);

	int countBySharedNoteId(Long sharedNoteId);

	List<UsedPencil> findAllByMemberOrderByCreatedAtDesc(Member member);

	@Query("select u.sharedNoteId from UsedPencil u where u.member = :member and u.sharedNoteId in :sharedNoteIds and u.type = 'OWNED'")
	List<Long> findByMemberInSharedNoteIdsAndTypeIsOwned(@Param("member") Member member,
		@Param("sharedNoteIds") List<Long> sharedNoteIds);

	@Query("select u from UsedPencil u where u.member = :member and u.type = 'OWNED' order by u.createdAt desc ")
	List<UsedPencil> findAllByMemberAndTypeIsOwnedOrderByCreatedAtDesc(@Param("member") Member member);

}
