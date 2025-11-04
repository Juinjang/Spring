package umc.th.juinjang.domain.limjang.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.member.model.Member;

@Repository
public interface LimjangRepository extends JpaRepository<Limjang, Long>, LimjangQueryDslRepository {

	@Query(value = "SELECT * FROM limjang l WHERE l.member_id = :memberId", nativeQuery = true)
	List<Limjang> findLimjangByMemberIdIgnoreDeleted(@Param("memberId") Long memberId);

	List<Limjang> findAllByLimjangIdInAndMemberIdAndDeletedIsFalse(List<Long> id, Member member);

	@Modifying
	@Query("UPDATE Limjang l SET l.deleted = true WHERE l.limjangId in :ids")
	void softDeleteByIds(@Param("ids") List<Long> ids);

	@Modifying
	@Query(value = "DELETE FROM limjang l WHERE l.deleted = true AND l.updated_at < :dateTime", nativeQuery = true)
	void hardDelete(@Param("dateTime") LocalDateTime dateTime);

	Optional<Limjang> findLimjangByLimjangIdAndMemberIdAndDeletedIsFalse(Long limjangId, Member member);

	@Modifying
	@Transactional
	@Query("UPDATE Limjang l SET l.recordCount = l.recordCount + 1 WHERE l.limjangId = :limjangId")
	void incrementRecordCount(@Param("limjangId") Long limjangId);

	@Modifying
	@Transactional
	@Query("UPDATE Limjang l SET l.memo = :memo WHERE l.limjangId = :limjangId")
	void updateMemo(@Param("limjangId") Long limjangId, @Param("memo") String memo);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM limjang l WHERE l.member_id = :memberId", nativeQuery = true)
	void deleteAllByMemberId(@Param("memberId") Long memberId);

	@Query("SELECT l FROM Limjang l join fetch l.limjangPrice WHERE l.limjangId = :id AND l.memberId = :member AND l.deleted = false")
	Optional<Limjang> findByLimjangIdAndMemberIdWithLimjangPriceAndDeletedIsFalse(@Param("id") Long id,
		@Param("member") Member member);

	@Query("SELECT l FROM Limjang l join fetch l.limjangPrice left join fetch l.report WHERE l.limjangId = :id AND l.memberId = :member AND l.deleted = false")
	Optional<Limjang> findByLimjangIdAndMemberAndDeletedIsFalse(@Param("id") Long id, @Param("member") Member member);

	@Query("SELECT l FROM Limjang l WHERE l.limjangId = :id AND l.deleted = false")
	Optional<Limjang> findByLimjangIdAndDeletedIsFalse(@Param("id") Long id);

	@Query("SELECT l FROM Limjang l left join fetch l.addressEntity left join fetch l.limjangPrice WHERE l.limjangId = :id AND l.deleted = false")
	Optional<Limjang> findByIdWithAddressAndNotePriceWhereDeletedIsFalse(@Param("id") Long id);

	@Query("SELECT l FROM Limjang l join fetch l.addressEntity join fetch l.limjangPrice left join fetch l.report WHERE l.memberId = :member AND l.deleted = false AND l.isSharable = true")
	List<Limjang> findAllByMemberWithAddressAndNotePriceWhereIsSharableIsTrueAndDeletedIsFalse(
		@Param("member") Member member);

	@Query("SELECT l FROM Limjang l join fetch l.addressEntity join fetch l.limjangPrice left join fetch l.report WHERE l.memberId = :member AND l.deleted = false AND l.addressEntity.bcode is not null AND l.isSharable = true")
	List<Limjang> findAllByMemberWithAddressAndNotePriceWhereIsSharableIsTrueAndDeletedIsFalseAndAddressBcodeIsNotNull(
		@Param("member") Member member);
}