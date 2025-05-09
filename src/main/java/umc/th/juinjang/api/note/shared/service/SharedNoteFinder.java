package umc.th.juinjang.api.note.shared.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.note.shared.controller.ExploreSortType;
import umc.th.juinjang.api.note.shared.controller.NoteType;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.SharedNoteHandler;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.member.model.Member;
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

	public SharedNote getBySharedNoteIdAndMember(Long sharedNoteId, Member member) {
		return sharedNoteRepository.findBySharedNoteIdAndMember(sharedNoteId, member).orElseThrow(
			() -> new SharedNoteHandler(ErrorStatus.SHAREDNOTE_NOT_FOUND));
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

	Page<SharedNote> findSharedNoteInExployer(List<String> code, ExploreSortType sort,
		LimjangPropertyType propertyType, LimjangPriceType priceType, String keyword, Pageable pageable) {
		return sharedNoteRepository.findSharedNoteInExployer(code, sort, propertyType, priceType,
			keyword, pageable);
	}

	public List<SharedNote> findUserSharedNotes(Member member, NoteType noteType, LimjangPropertyType propertyType,
		LimjangPriceType priceType, String keyword, List<Long> filterIds) {
		return sharedNoteRepository.findUserSharedNotes(member, noteType, propertyType, priceType, keyword, filterIds);
	}
}
