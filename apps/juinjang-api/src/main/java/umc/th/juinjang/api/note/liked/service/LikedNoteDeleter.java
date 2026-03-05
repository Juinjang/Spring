package umc.th.juinjang.api.note.liked.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.note.liked.model.LikedNote;
import umc.th.juinjang.domain.note.liked.model.repository.LikedNoteRepository;

@Component
@RequiredArgsConstructor
public class LikedNoteDeleter {

	private final LikedNoteRepository likedNoteRepository;

	void delete(LikedNote likedNote) {
		likedNoteRepository.delete(likedNote);
	}
}
