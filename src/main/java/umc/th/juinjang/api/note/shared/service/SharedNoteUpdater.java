package umc.th.juinjang.api.note.shared.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.note.shared.repository.SharedNoteRepository;

@Component
@RequiredArgsConstructor
public class SharedNoteUpdater {

	private final SharedNoteRepository sharedNoteRepository;

	void updateViewCount(long sharedNoteId, long addAmount) {
		sharedNoteRepository.incrementViewCount(sharedNoteId, addAmount);
	}
}
