package umc.th.juinjang.api.note.shared.service;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.SharedNoteHandler;
import umc.th.juinjang.domain.note.shared.model.SharedNote;
import umc.th.juinjang.domain.note.shared.repository.SharedNoteRepository;

@Component
@RequiredArgsConstructor
public class SharedNoteFinder {

	private final SharedNoteRepository sharedNoteRepository;

	public SharedNote getById(Long id) {
		return sharedNoteRepository.findById(id)
			.orElseThrow(() -> new SharedNoteHandler(ErrorStatus.SHAREDNOTE_NOT_FOUND));
	}

	SharedNote findByIdWithNoteAndAddress(Long id) {
		return sharedNoteRepository.findByIdWithNoteAndAddress(id)
			.orElseThrow(() -> new SharedNoteHandler(ErrorStatus.SHAREDNOTE_NOT_FOUND));
	}

	Optional<SharedNote> findById(Long id) {
		return sharedNoteRepository.findById(id);
	}

	public Long getLikedNoteById(Long id) {
		return sharedNoteRepository.getLikeCountById(id);
	}

}
