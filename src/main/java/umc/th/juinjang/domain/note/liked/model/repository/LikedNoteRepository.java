package umc.th.juinjang.domain.note.liked.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.note.liked.model.LikedNote;
import umc.th.juinjang.domain.note.shared.model.SharedNote;

public interface LikedNoteRepository extends JpaRepository<LikedNote, Long> {

	boolean existsByMemberAndSharedNote(Member member, SharedNote sharedNote);

	Optional<LikedNote> findByMemberAndSharedNote(Member member, SharedNote sharedNote);
}
