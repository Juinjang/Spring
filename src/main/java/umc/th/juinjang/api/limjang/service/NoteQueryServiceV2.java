package umc.th.juinjang.api.limjang.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.checklist.service.ChecklistAnswerFinder;
import umc.th.juinjang.api.image.service.ImageFinder;
import umc.th.juinjang.api.limjang.controller.parameter.LimjangSortOptions;
import umc.th.juinjang.api.limjang.service.response.ChecklistConditionResponse;
import umc.th.juinjang.api.limjang.service.response.UserNoteGetResponse;
import umc.th.juinjang.api.limjang.service.response.UserNotesGetResponse;
import umc.th.juinjang.api.limjang.service.response.UserNotesShareableGetResponse;
import umc.th.juinjang.api.note.shared.service.SharedNoteFinder;
import umc.th.juinjang.api.scrap.service.ScarpFinder;
import umc.th.juinjang.domain.checklist.model.ChecklistAnswer;
import umc.th.juinjang.domain.checklist.model.ChecklistQuestionCategory;
import umc.th.juinjang.domain.image.model.Image;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.model.LimjangPurpose;
import umc.th.juinjang.domain.member.model.Member;

@Service
@RequiredArgsConstructor
public class NoteQueryServiceV2 {

	private final NoteFinder noteFinder;
	private final ScarpFinder scarpFinder;
	private final ImageFinder imageFinder;
	private final ChecklistAnswerFinder checklistAnswerFinder;
	private final SharedNoteFinder sharedNoteFinder;

	@Transactional(readOnly = true)
	public UserNotesGetResponse findUsersNotes(Member member, LimjangSortOptions sortOptions, String keyword) {
		List<Limjang> notes = noteFinder.findAllByMemberOrderByOptions(member, sortOptions, keyword);
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
		List<Limjang> filteredSharedNotes = findUnsharedSharableNotes(member);
		List<Image> imageList = imageFinder.findAllFirstCreatedImagePerNote(filteredSharedNotes);

		return UserNotesShareableGetResponse.of(filteredSharedNotes, mapToNoteIdAndImageId(imageList),
			mapToNoteScrapStatus(filteredSharedNotes));
	}

	private List<Limjang> findUnsharedSharableNotes(Member member) {
		List<Limjang> notes = noteFinder.getAllByMemberWithAddressAndNotePriceWhereIsSharableIsTrueAndDeletedIsFalseAndAddressBcodeIsNotNull(
			member);
		Set<Long> noteIdInSharedNotes = sharedNoteFinder.findLimjangIdsByDeletedAtIsNullAndLimjang(notes);

		return notes.stream()
			.filter(note -> !noteIdInSharedNotes.contains(note.getLimjangId()))
			.toList();
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
		Limjang note = noteFinder.getNoteByIdWithAddressAndNotePriceWhereDeletedIsFalse(noteId);
		boolean isShared = sharedNoteFinder.existsByDeletedAtIsNullAndLimjang(note);
		return UserNoteGetResponse.of(isShared, note);
	}

	public ChecklistConditionResponse checkLimjangChecklistSatisfaction(Long limjangId) {
		Limjang limjang = noteFinder.getNoteByIdWhereDeletedIsFalse(limjangId);
		LimjangPurpose purpose = limjang.getPurpose();
		List<ChecklistAnswer> answers = checklistAnswerFinder.findEntitiesByLimjangId(limjangId);

		Map<ChecklistQuestionCategory, Long> answeredCountByCategory = answers.stream()
			.collect(Collectors.groupingBy(
				a -> a.getQuestionId().getCategory(),
				Collectors.counting()
			));

		List<ChecklistConditionResponse.CategoryCondition> results = new ArrayList<>();
		boolean allSatisfied = true;

		for (ChecklistQuestionCategory category : List.of(
			ChecklistQuestionCategory.LOCATION_CONDITION,
			ChecklistQuestionCategory.PUBLIC_SPACE,
			ChecklistQuestionCategory.INDOOR
		)) {
			int totalCount = getTotalCount(purpose, category);
			int requiredCount = getRequiredCount(purpose, category);
			int answeredCount = answeredCountByCategory.getOrDefault(category, 0L).intValue();

			boolean satisfied = answeredCount >= requiredCount;
			if (!satisfied)
				allSatisfied = false;

			results.add(new ChecklistConditionResponse.CategoryCondition(
				category.name(), answeredCount, totalCount, requiredCount, satisfied
			));
		}

		return new ChecklistConditionResponse(allSatisfied, results);
	}

	private int getTotalCount(LimjangPurpose purpose, ChecklistQuestionCategory category) {
		return switch (purpose) {
			case INVESTMENT -> switch (category) {
				case LOCATION_CONDITION -> 19;
				case PUBLIC_SPACE -> 8;
				case INDOOR -> 21;
				default -> 0;
			};
			case RESIDENTIAL_PURPOSE -> switch (category) {
				case LOCATION_CONDITION -> 9;
				case PUBLIC_SPACE -> 6;
				case INDOOR -> 20;
				default -> 0;
			};
		};
	}

	private int getRequiredCount(LimjangPurpose purpose, ChecklistQuestionCategory category) {
		return switch (purpose) {
			case INVESTMENT -> switch (category) {
				case LOCATION_CONDITION -> 16;
				case PUBLIC_SPACE -> 5;
				case INDOOR -> 18;
				default -> 0;
			};
			case RESIDENTIAL_PURPOSE -> switch (category) {
				case LOCATION_CONDITION -> 7;
				case PUBLIC_SPACE -> 4;
				case INDOOR -> 18;
				default -> 0;
			};
		};
	}
}
