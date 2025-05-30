package umc.th.juinjang.api.note.shared.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.note.shared.model.SharedNote;
import umc.th.juinjang.domain.note.shared.repository.SharedNoteRepository;

@Component
@RequiredArgsConstructor
public class SharedNoteUpdater {

	private final SharedNoteRepository sharedNoteRepository;

	void updateViewCount(long sharedNoteId, long addAmount) {
		sharedNoteRepository.incrementViewCount(sharedNoteId, addAmount);
	}

	public void incrementLikedCountById(Long sharedNoteId) {
		sharedNoteRepository.incrementLikedCountById(sharedNoteId);
	}

	public void decrementLikedCountById(Long sharedNoteId) {
		sharedNoteRepository.decrementLikedCountById(sharedNoteId);
	}

	public SharedNote save(SharedNote sharedNote) {
		return sharedNoteRepository.save(sharedNote);
	}
}
