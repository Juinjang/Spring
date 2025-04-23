package umc.th.juinjang.domain.note.shared.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import umc.th.juinjang.domain.note.shared.model.SharedNote;

public interface SharedNoteRepository extends JpaRepository<SharedNote, Long> {

	@Query("select s from SharedNote s join fetch s.limjang l join fetch l.addressEntity join fetch l.limjangPrice where s.sharedNoteId = :sharedNoteId")
	Optional<SharedNote> findByIdWithNoteAndAddress(@Param("sharedNoteId") Long sharedNoteId);

	@Modifying
	@Query("UPDATE SharedNote s SET s.viewCount = COALESCE(s.viewCount, 0) + :addAmount WHERE s.sharedNoteId = :sharedNoteId")
	void incrementViewCount(@Param("sharedNoteId") Long sharedNoteId, @Param("addAmount") Long addAmount);
}