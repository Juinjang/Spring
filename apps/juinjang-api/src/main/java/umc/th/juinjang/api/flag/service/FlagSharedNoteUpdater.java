package umc.th.juinjang.api.flag.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.flag.model.FlagSharedNote;
import umc.th.juinjang.domain.flag.repository.FlagSharedNoteRepository;

@Component
@RequiredArgsConstructor
public class FlagSharedNoteUpdater {

	private final FlagSharedNoteRepository flagSharedNoteRepository;

	void save(FlagSharedNote flagSharedNote) {
		flagSharedNoteRepository.save(flagSharedNote);
	}
}
