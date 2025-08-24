package umc.th.juinjang.api.note.shared.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class SharedNoteQueryService {

    private final UsedPencilFinder usedPencilFinder;
    private final SharedNoteFinder sharedNoteFinder;
    private final LikedNoteFinder likedNoteFinder;
    private final ChecklistAnswerFinder checklistAnswerFinder;
    private final ViewCountService viewCountService;
    private final ReportFinder reportFinder;

    @Transactional
    public SharedNoteGetResponse findSharedNote(Member member, Long sharedNoteId) {
        SharedNote sharedNote = sharedNoteFinder.findByIdWithNoteAndAddress(sharedNoteId);
        Limjang limjang = sharedNote.getLimjang();

        boolean isBuyerOrOwner = getIsBuyerOrOwner(member, sharedNote);

        long viewCount = viewCountService.getViewCount(member, sharedNote);

        Integer countBuyer = makeBuyerCount(usedPencilFinder.countBySharedNoteId(sharedNoteId));
        boolean isLiked = likedNoteFinder.existsByMemberAndSharedNote(member, sharedNote);

        if (isBuyerOrOwner) {
            return SharedNoteGetResponse.ofPurchased(isBuyerOrOwner, limjang, limjang.getAddressEntity(), sharedNote,
                    sharedNote.getMember(), countBuyer, isLiked, viewCount);
        } else {
            return SharedNoteGetResponse.ofNotPurchased(isBuyerOrOwner, limjang, limjang.getAddressEntity(), sharedNote,
                    sharedNote.getMember(), countBuyer, isLiked, viewCount);
        }
    }

    private boolean getIsBuyerOrOwner(Member requestMember, SharedNote sharedNote) {
        return usedPencilFinder.existsByMemberAndSharedNoteId(requestMember, sharedNote.getSharedNoteId()) ||
                sharedNote.getMember().getMemberId().equals(requestMember.getMemberId());
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
                viewcountMap, member.getMemberId());
    }

    private Map<Long, Long> mapIdsAndViewcount(List<SharedNote> sharedNotes) {
        return sharedNotes.stream().collect(Collectors.toMap(
                SharedNote::getSharedNoteId,
                SharedNote::getViewCount
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

        return UserSharedNotesGetResponse.ofLiked(sharedNotes, purchasedIds, viewcountMap, member.getMemberId());
    }

    @Transactional(readOnly = true)
    public SharedNoteCheckListAndReviewResponse findChecklistAndReview(Member member, Long sharedNoteId) {
        SharedNote sharedNote = sharedNoteFinder.findByIdWithNoteAndAddress(sharedNoteId);
        Limjang note = sharedNote.getLimjang();
        List<ChecklistAnswerResponseDTO.AnswerDto> answers = checklistAnswerFinder.findByLimjangId(
                note.getLimjangId());

        Report report = reportFinder.findReportByNote(note);

        boolean isBuyerOrOwner = getIsBuyerOrOwner(member, sharedNote);
        // 구매했다면 review 포함, 아니면 null
        String review = isBuyerOrOwner ? sharedNote.getReview() : null;
        Float totalRate = isBuyerOrOwner ? report.getTotalRate() : null;
        return new SharedNoteCheckListAndReviewResponse(review, totalRate, answers);
    }

    public ReportWithLimjangResponseDTO getReportBySharedNoteId(Long sharedNoteId) {
        SharedNote sharedNote = sharedNoteFinder.findByIdWithNoteAndAddress(sharedNoteId);
        Limjang note = sharedNote.getLimjang();
        Report report = reportFinder.findReportByNote(note);
        return new ReportWithLimjangResponseDTO(ReportGetResponse.of(report), LimjangDetailGetResponse.of(note));
    }
}
