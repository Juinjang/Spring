package umc.th.juinjang.domain.pencil.acquired.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import umc.th.juinjang.api.pencil.service.response.AcquiredPencilResponse;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredPencil;

public interface AcquiredPencilRepository extends JpaRepository<AcquiredPencil, Long> {

	@Query("SELECT new umc.th.juinjang.api.pencil.service.response.AcquiredPencilResponse(" +
		"ap.id, ap.content, ap.sharedNoteId, ap.acquiredQuantity, " +
		"ap.isRead, ap.type, ap.createdAt, sn.buildingName) " +
		"FROM AcquiredPencil ap " +
		"LEFT JOIN SharedNote sn ON ap.sharedNoteId = sn.sharedNoteId " +
		"WHERE ap.member = :member " +
		"ORDER BY ap.createdAt DESC")
	List<AcquiredPencilResponse> findAllByMemberWithBuildingNameOrderByCreatedAtDesc(@Param("member") Member member);

	boolean existsByMemberAndIsReadFalse(Member member);

	boolean existsByMember(Member member);
}
