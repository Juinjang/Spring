package umc.th.juinjang.domain.note.shared.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import umc.th.juinjang.domain.note.shared.model.SharedNote;

public interface SharedNoteRepository extends JpaRepository<SharedNote, Long> {
}
