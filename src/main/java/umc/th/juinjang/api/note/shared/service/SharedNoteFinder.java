package umc.th.juinjang.api.note.shared.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.note.shared.controller.request.ExploreSortType;
import umc.th.juinjang.api.note.shared.controller.request.NoteType;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.SharedNoteHandler;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.note.shared.model.SharedNote;
import umc.th.juinjang.domain.note.shared.repository.SharedNoteRepository;

@Component
@RequiredArgsConstructor
public class SharedNoteFinder {

	private final SharedNoteRepository sharedNoteRepository;

	public SharedNote getByIdWhereDeletedAtIsNull(Long id) {
		return sharedNoteRepository.findBySharedNoteIdAndDeletedAtIsNull(id)
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

	public Optional<SharedNote> findLatestByLimjangId(Long limjangId) {
		return sharedNoteRepository.findLatestByLimjangId(limjangId);
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

	public Map<Long, Long> findAllIdAndViewCountById(List<Long> ids) {
		return sharedNoteRepository.findAllViewCountById(ids).stream()
			.collect(Collectors.toMap(
				row -> (Long)row[0],
				row -> (Long)row[1]
			));
	}

	public Long findViewCountById(Long id) {
		return sharedNoteRepository.findViewCountById(id);
	}

	public boolean existsByDeletedAtIsNullAndLimjang(Limjang limjang) {
		return sharedNoteRepository.existsByDeletedAtIsNullAndLimjang(limjang);
	}
}
