package umc.th.juinjang.api.note.shared.service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.api.checklist.service.ChecklistAnswerFinder;
import umc.th.juinjang.api.checklist.service.ReportFinder;
import umc.th.juinjang.api.checklist.service.response.ChecklistAnswerResponseDTO;
import umc.th.juinjang.api.checklist.service.response.ReportGetResponse;
import umc.th.juinjang.api.checklist.service.response.ReportWithLimjangResponseDTO;
import umc.th.juinjang.api.limjang.service.response.LimjangDetailGetResponse;
import umc.th.juinjang.api.note.liked.service.LikedNoteFinder;
import umc.th.juinjang.api.note.shared.controller.request.ExploreSortType;
import umc.th.juinjang.api.note.shared.controller.request.NoteType;
import umc.th.juinjang.api.note.shared.service.response.SharedNoteCheckListAndReviewResponse;
import umc.th.juinjang.api.note.shared.service.response.SharedNoteExploreGetResponse;
import umc.th.juinjang.api.note.shared.service.response.SharedNoteGetResponse;
import umc.th.juinjang.api.note.shared.service.response.UserSharedNotesGetResponse;
import umc.th.juinjang.api.pencil.service.UsedPencilFinder;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.SharedNoteHandler;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.note.liked.model.LikedNote;
import umc.th.juinjang.domain.note.shared.model.SharedNote;
import umc.th.juinjang.domain.pencil.used.model.UsedPencil;
import umc.th.juinjang.domain.report.model.Report;
import umc.th.juinjang.event.publisher.ApplicationRewardViewCountPublisherAdapter;

@Service
@Slf4j
@RequiredArgsConstructor
public class SharedNoteQueryService {

	private final UsedPencilFinder usedPencilFinder;
	private final SharedNoteFinder sharedNoteFinder;
	private final LikedNoteFinder likedNoteFinder;
	private final ChecklistAnswerFinder checklistAnswerFinder;
	private final ViewCountService viewCountService;
	private final ApplicationRewardViewCountPublisherAdapter applicationRewardViewCountPublisherAdapter;
	private final ReportFinder reportFinder;

	@Transactional(readOnly = true)
	public SharedNoteGetResponse findSharedNote(Member member, Long sharedNoteId) {
		SharedNote sharedNote = sharedNoteFinder.findByIdWithNoteAndAddress(sharedNoteId);
		Limjang limjang = sharedNote.getLimjang();

		boolean isBuyer = usedPencilFinder.existsByMemberAndSharedNoteId(member, sharedNoteId);

		long viewCount = getViewCountAndCheckReward(member, sharedNoteId, sharedNote);

		Integer countBuyer = makeBuyerCount(usedPencilFinder.countBySharedNoteId(sharedNoteId));
		boolean isLiked = likedNoteFinder.existsByMemberAndSharedNote(member, sharedNote);

		if (isBuyer) {
			return SharedNoteGetResponse.ofPurchased(isBuyer, limjang, limjang.getAddressEntity(), sharedNote,
				sharedNote.getMember(), countBuyer, isLiked, viewCount);
		} else {
			return SharedNoteGetResponse.ofNotPurchased(isBuyer, limjang, limjang.getAddressEntity(), sharedNote,
				sharedNote.getMember(), countBuyer, isLiked, viewCount);
		}
	}

	private long getViewCountAndCheckReward(Member member, Long sharedNoteId, SharedNote sharedNote) {
		long viewCount = viewCountService.getRedisViewCount(sharedNote.getSharedNoteId());
		if (!viewCountService.isDuplicate(member.getMemberId(), sharedNoteId)) {
			viewCountService.increaseViewCount(sharedNoteId);
			viewCount++;
			viewCountService.recordViewerHistory(member.getMemberId(), sharedNoteId);

			applicationRewardViewCountPublisherAdapter.checkViewCountRewardPolicy(sharedNote.getMember(),
				sharedNote.getSharedNoteId(), viewCount);
		}
		return viewCount;
	}

	private Integer makeBuyerCount(int count) {
		if (count >= 100) {
			return 100;
		} else if (count >= 50) {
			return 50;
		} else if (count >= 30) {
			return 30;
		} else if (count >= 10) {
			return 10;
		}
		return null;
	}

	@Transactional(readOnly = true)
	public SharedNoteExploreGetResponse findExploreSharedNote(Member member, List<String> code,
		ExploreSortType sort,
		LimjangPropertyType propertyType, LimjangPriceType priceType, String keyword,
		Pageable pageable) {

		Page<SharedNote> pages = sharedNoteFinder.findSharedNoteInExployer(code, sort, propertyType, priceType, keyword,
			pageable);
		List<SharedNote> sharedNotes = pages.getContent();
		List<Long> ids = sharedNotes.stream().map(SharedNote::getSharedNoteId).toList();

		Set<Long> likedNoteIds = new HashSet<>(likedNoteFinder.findLikedSharedNoteIds(member, sharedNotes));
		Set<Long> purchasedIds = new HashSet<>(usedPencilFinder.findByMemberInSharedNoteIdsAndTypeIsOwned(member, ids));

		Map<Long, Long> viewcountMap = mapIdsAndViewcount(sharedNotes);
		return SharedNoteExploreGetResponse.of(pages.getTotalElements(), sharedNotes, purchasedIds, likedNoteIds,
			viewcountMap);
	}

	private Map<Long, Long> mapIdsAndViewcount(List<SharedNote> sharedNotes) {
		return sharedNotes.stream().collect(Collectors.toMap(
			SharedNote::getSharedNoteId,
			it -> viewCountService.getRedisViewCount(it.getSharedNoteId())
		));
	}

	public UserSharedNotesGetResponse findUserSharedNotes(Member member, NoteType noteType,
		LimjangPropertyType propertyType, LimjangPriceType priceType, String keyword) {

		switch (noteType) {
			case LIKED -> {
				return getUserLikedSharedNotes(member, propertyType, priceType, keyword);
			}
			case SHARED -> {
				return getUserSharedNotes(member, noteType, propertyType, priceType, keyword);
			}
			case OWNED -> {
				return getUserOwnedSharedNotes(member, noteType, propertyType, priceType, keyword);
			}
			default -> throw new SharedNoteHandler(ErrorStatus.SHAREDNOTE_TYPE_ERROR);
		}
	}

	private UserSharedNotesGetResponse getUserOwnedSharedNotes(Member member, NoteType noteType,
		LimjangPropertyType propertyType, LimjangPriceType priceType, String keyword) {

		List<UsedPencil> usedPencils = usedPencilFinder.findAllByMemberAndTypeIsOwnedOrderByCreatedAtDesc(member);
		List<Long> sharedNoteIds = usedPencils.stream().map(UsedPencil::getSharedNoteId).toList();

		List<SharedNote> sharedNotes = sharedNoteFinder.findUserSharedNotes(member, noteType, propertyType, priceType,
			keyword, sharedNoteIds);
		List<SharedNote> sortedSharedNotes = sortByUsedPencilCreatedAt(sharedNoteIds, sharedNotes);

		Map<Long, Long> viewcountMap = mapIdsAndViewcount(sharedNotes);
		Set<Long> likedNoteIds = new HashSet<>(likedNoteFinder.findLikedSharedNoteIds(member, sharedNotes));

		return UserSharedNotesGetResponse.ofOwned(sortedSharedNotes, likedNoteIds, viewcountMap);
	}

	private List<SharedNote> sortByUsedPencilCreatedAt(List<Long> sharedNoteIds, List<SharedNote> sharedNotes) {
		Map<Long, Integer> orderMap = IntStream.range(0, sharedNoteIds.size())
			.boxed()
			.collect(Collectors.toMap(sharedNoteIds::get, i -> i));

		return sharedNotes.stream()
			.sorted(Comparator.comparingInt(note -> orderMap.get(note.getSharedNoteId())))
			.toList();
	}

	private UserSharedNotesGetResponse getUserSharedNotes(Member member, NoteType noteType,
		LimjangPropertyType propertyType, LimjangPriceType priceType, String keyword) {
		List<SharedNote> sharedNotes = sharedNoteFinder.findUserSharedNotes(member, noteType, propertyType, priceType,
			keyword, List.of());
		Map<Long, Long> viewcountMap = mapIdsAndViewcount(sharedNotes);

		Set<Long> likedNoteIds = new HashSet<>(likedNoteFinder.findLikedSharedNoteIds(member, sharedNotes));
		return UserSharedNotesGetResponse.ofShared(member, sharedNotes, likedNoteIds, viewcountMap);
	}

	private UserSharedNotesGetResponse getUserLikedSharedNotes(Member member, LimjangPropertyType propertyType,
		LimjangPriceType priceType, String keyword) {

		List<LikedNote> userLikedNotes = likedNoteFinder.findAllByMemberAndDynamic(member, propertyType,
			priceType, keyword);
		List<SharedNote> sharedNotes = userLikedNotes.stream().map(LikedNote::getSharedNote).toList();

		Set<Long> purchasedIds = new HashSet<>(usedPencilFinder.findByMemberInSharedNoteIdsAndTypeIsOwned(member,
			sharedNotes.stream().map(SharedNote::getSharedNoteId).toList()));
		Map<Long, Long> viewcountMap = mapIdsAndViewcount(sharedNotes);

		return UserSharedNotesGetResponse.ofLiked(sharedNotes, purchasedIds, viewcountMap);
	}

	@Transactional(readOnly = true)
	public SharedNoteCheckListAndReviewResponse findChecklistAndReview(Member member, Long sharedNoteId) {
		SharedNote sharedNote = sharedNoteFinder.findByIdWithNoteAndAddress(sharedNoteId);
		Limjang note = sharedNote.getLimjang();
		List<ChecklistAnswerResponseDTO.AnswerDto> answers = checklistAnswerFinder.findByLimjangId(
			note.getLimjangId());

		Report report = reportFinder.findReportByNote(note);

		boolean isOwned = usedPencilFinder.existsByMemberAndSharedNoteId(member, sharedNoteId);
		// 구매했다면 review 포함, 아니면 null
		String review = isOwned ? sharedNote.getReview() : null;
		Float totalRate = isOwned ? report.getTotalRate() : null;
		return new SharedNoteCheckListAndReviewResponse(review, totalRate, answers);
	}

	public ReportWithLimjangResponseDTO getReportBySharedNoteId(Long sharedNoteId) {
		SharedNote sharedNote = sharedNoteFinder.findByIdWithNoteAndAddress(sharedNoteId);
		Limjang note = sharedNote.getLimjang();
		Report report = reportFinder.findReportByNote(note);
		return new ReportWithLimjangResponseDTO(ReportGetResponse.of(report), LimjangDetailGetResponse.of(note));
	}
}
