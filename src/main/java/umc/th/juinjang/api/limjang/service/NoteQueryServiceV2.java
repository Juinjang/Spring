package umc.th.juinjang.api.limjang.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.image.service.ImageFinder;
import umc.th.juinjang.api.limjang.controller.parameter.LimjangSortOptions;
import umc.th.juinjang.api.limjang.service.response.UserNoteGetResponse;
import umc.th.juinjang.api.limjang.service.response.UserNotesShareableGetResponse;
import umc.th.juinjang.api.limjang.service.response.UserNotesGetResponse;
import umc.th.juinjang.api.scrap.service.ScarpFinder;
import umc.th.juinjang.domain.image.model.Image;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.member.model.Member;

@Service
@RequiredArgsConstructor
public class NoteQueryServiceV2 {

	private final NoteFinder noteFinder;
	private final ScarpFinder scarpFinder;
	private final ImageFinder imageFinder;

	@Transactional(readOnly = true)
	public UserNotesGetResponse findUsersNotes(Member member, LimjangSortOptions sortOptions) {
		List<Limjang> notes = noteFinder.findAllByMemberOrderByOptions(member, sortOptions);
		return UserNotesGetResponse.of(notes, mapToNoteScrapStatus(notes));
	}

	private Map<Long, Boolean> mapToNoteScrapStatus(List<Limjang> notes) {
		Set<Long> notesIdInScrap = getNotesIdInScraps(notes);
		return notes.stream().collect(Collectors.toMap(
			Limjang::getLimjangId,
			it -> notesIdInScrap.contains(it.getLimjangId())
		));
	}

	private Set<Long> getNotesIdInScraps(List<Limjang> notes) {
		return new HashSet<>(scarpFinder.findAllByNoteId(notes)
			.stream()
			.map(it -> it.getLimjangId().getLimjangId())
			.toList());
	}

	@Transactional(readOnly = true)
	public UserNotesShareableGetResponse findNotesShareable(Member member) {
		List<Limjang> notes = noteFinder.getAllByMemberWithAddressAndNotePriceWhereRewardPencilIsNotNullAndDeletedIsFalse(
			member);

		List<Image> imageList = imageFinder.findAllFirstCreatedImagePerNote(notes);

		return UserNotesShareableGetResponse.of(notes, mapToNoteIdAndImageId(imageList), mapToNoteScrapStatus(notes));
	}

	private Map<Long, String> mapToNoteIdAndImageId(List<Image> imageList) {
		return imageList.stream()
			.collect(Collectors.toMap(
				image -> image.getLimjangId().getLimjangId(),
				image -> image.getImageUrl()
			));
	}

	@Transactional(readOnly = true)
	public UserNoteGetResponse findNote(Long noteId) {
		return UserNoteGetResponse.of(noteFinder.getNoteByIdWithAddressAndNotePriceWhereDeletedIsFalse(noteId));
	}
}
