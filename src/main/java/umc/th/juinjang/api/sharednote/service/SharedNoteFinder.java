package umc.th.juinjang.api.sharednote.service;

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

	SharedNote findById(Long id) {
		return sharedNoteRepository.findById(id)
			.orElseThrow(() -> new SharedNoteHandler(ErrorStatus.SHAREDNOTE_NOT_FOUND));
	}
}
