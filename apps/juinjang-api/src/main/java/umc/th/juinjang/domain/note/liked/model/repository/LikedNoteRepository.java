package umc.th.juinjang.domain.note.liked.model.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.note.liked.model.LikedNote;
import umc.th.juinjang.domain.note.shared.model.SharedNote;

public interface LikedNoteRepository extends JpaRepository<LikedNote, Long>, LikedNoteQueryDSLRepository {

	boolean existsByMemberAndSharedNote(Member member, SharedNote sharedNote);

	Optional<LikedNote> findByMemberAndSharedNote(Member member, SharedNote sharedNote);

	@Query("SELECT l.sharedNote.sharedNoteId FROM LikedNote l WHERE l.member = :member AND l.sharedNote IN :sharedNotes")
	List<Long> findLikedSharedNoteIds(@Param("member") Member member,
		@Param("sharedNotes") List<SharedNote> sharedNotes);

	List<LikedNote> findAllByMember(Member member);
}
