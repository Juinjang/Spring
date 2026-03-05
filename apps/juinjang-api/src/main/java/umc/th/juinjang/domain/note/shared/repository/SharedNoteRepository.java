package umc.th.juinjang.domain.note.shared.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.note.shared.model.SharedNote;

public interface SharedNoteRepository extends JpaRepository<SharedNote, Long>, SharedNoteQueryDSLRepository {

	@Query("select s from SharedNote s join fetch s.limjang l join fetch l.addressEntity join fetch l.limjangPrice where s.sharedNoteId = :sharedNoteId")
	Optional<SharedNote> findByIdWithNoteAndAddress(@Param("sharedNoteId") Long sharedNoteId);

	@Modifying
	@Query("UPDATE SharedNote s SET s.viewCount = s.viewCount + 1 WHERE s.sharedNoteId = :sharedNoteId")
	void incrementViewCount(@Param("sharedNoteId") Long sharedNoteId);

	@Modifying
	@Query("UPDATE SharedNote sn SET sn.likeCount = sn.likeCount + 1 WHERE sn.sharedNoteId = :sharedNoteId")
	void incrementLikedCountById(@Param("sharedNoteId") Long sharedNoteId);

	@Query("SELECT sn.likeCount FROM SharedNote sn WHERE sn.sharedNoteId = :sharedNoteId")
	Long getLikeCountById(@Param("sharedNoteId") Long sharedNoteId);

	@Modifying
	@Query("UPDATE SharedNote sn SET sn.likeCount = sn.likeCount - 1 WHERE sn.sharedNoteId = :sharedNoteId")
	void decrementLikedCountById(@Param("sharedNoteId") Long sharedNoteId);

	Optional<SharedNote> findTop1ByLimjang_LimjangIdOrderByCreatedAtDesc(Long limjangId);

	Optional<SharedNote> getBySharedNoteIdAndMemberAndDeletedAtIsNull(Long sharedNoteId, Member member);

	@Query("SELECT s.sharedNoteId, s.viewCount FROM SharedNote s WHERE s.sharedNoteId IN :ids")
	List<Object[]> findAllViewCountById(@Param("ids") List<Long> ids);

	@Query("SELECT s.viewCount FROM SharedNote s WHERE s.sharedNoteId = :id")
	Long findViewCountById(@Param("id") Long id);

	Optional<SharedNote> findBySharedNoteIdAndDeletedAtIsNull(Long id);

	boolean existsByDeletedAtIsNullAndLimjang(Limjang limjang);

	@Query("SELECT s.limjang.limjangId FROM SharedNote s WHERE s.limjang in :limjangs AND s.deletedAt is null")
	Set<Long> findLimjangIdsByDeletedAtIsNullAndLimjang(@Param("limjangs") List<Limjang> limjangs);

	@Query("SELECT s.limjang.limjangId FROM SharedNote s WHERE s.limjang in :limjangs AND s.deletedAt is not null")
	Set<Long> findLimjangIdsByDeletedAtIsNotNullAndLimjang(@Param("limjangs") List<Limjang> limjangs);

	@Query("SELECT s.sharedNoteId, s.buildingName FROM SharedNote s WHERE s.sharedNoteId IN :ids")
	List<Object[]> findAllBuildingNameById(@Param("ids") List<Long> ids);

	default Map<Long, String> findBuildingNameMapByIds(List<Long> ids) {
		return findAllBuildingNameById(ids).stream()
			.collect(Collectors.toMap(
				row -> (Long) row[0],
				row -> (String) row[1],
				(a, b) -> a
			));
	}
}
