package umc.th.juinjang.api.note.liked.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.LikedNoteHandler;
import umc.th.juinjang.common.exception.handler.SharedNoteHandler;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.note.liked.model.LikedNote;
import umc.th.juinjang.domain.note.liked.model.repository.LikedNoteRepository;

@Component
@RequiredArgsConstructor
public class LikedNoteUpdater {

	private final LikedNoteRepository likedNoteRepository;

	public void save(LikedNote likedNote) {
		try {
			likedNoteRepository.save(likedNote);
		} catch (DataIntegrityViolationException e) {
			throw new LikedNoteHandler(ErrorStatus.LIKEDNOTE_CONFLICT);
		}
	}
}
