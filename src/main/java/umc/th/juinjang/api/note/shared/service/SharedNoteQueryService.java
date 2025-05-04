package umc.th.juinjang.api.note.shared.service;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.ErrorHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.api.note.liked.service.LikedNoteFinder;
import umc.th.juinjang.api.note.shared.controller.ExploreSortType;
import umc.th.juinjang.api.note.shared.controller.NoteType;
import umc.th.juinjang.api.note.shared.service.response.SharedNoteExploreGetResponse;
import umc.th.juinjang.api.note.shared.service.response.UserSharedNotesGetResponse;
import umc.th.juinjang.api.pencil.service.UsedPencilFinder;
import umc.th.juinjang.api.note.shared.service.response.SharedNoteGetResponse;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.SharedNoteHandler;
import umc.th.juinjang.common.redis.RedisKeyFactory;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.note.liked.model.LikedNote;
import umc.th.juinjang.domain.note.shared.model.SharedNote;
import umc.th.juinjang.domain.pencil.used.model.UsedPencil;

@Service
@Slf4j
@RequiredArgsConstructor
public class SharedNoteQueryService {

	private final UsedPencilFinder usedPencilFinder;
	private final SharedNoteFinder sharedNoteFinder;
	private final LikedNoteFinder likedNoteFinder;
	private final RedisTemplate<String, String> redisTemplate;

	@Transactional(readOnly = true)
	public SharedNoteGetResponse findSharedNote(Member member, Long sharedNoteId) {
		SharedNote sharedNote = sharedNoteFinder.findByIdWithNoteAndAddress(sharedNoteId);
		Limjang limjang = sharedNote.getLimjang();

		boolean isBuyer = usedPencilFinder.existsByMemberAndSharedNoteId(member, sharedNoteId);

		long viewCount = getTotalViewCount(sharedNote);
		if (!isDuplicate(member.getMemberId(), sharedNoteId)) {
			increaseViewCount(sharedNoteId);
			viewCount++;
			recordViewerHistory(member.getMemberId(), sharedNoteId);
		}

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

	private long getTotalViewCount(SharedNote sharedNote) {
		return sharedNote.getViewCount() + getRedisViewCount(sharedNote.getSharedNoteId());
	}

	private void recordViewerHistory(long memberId, long sharedNoteId) {
		try {
			redisTemplate.opsForValue()
				.setIfAbsent(RedisKeyFactory.viewHistoryKey(sharedNoteId, memberId), "1", Duration.ofHours(3));
		} catch (RedisConnectionFailureException | RedisSystemException e) {
			log.error("Redis 연결 실패 - 중복 조회 기록 불가, sharedNoteId={}, memberId={}", sharedNoteId, memberId, e);
		}
	}

	private void increaseViewCount(long sharedNoteId) {
		try {
			redisTemplate.opsForValue().increment(RedisKeyFactory.viewCountKey(sharedNoteId));
		} catch (RedisConnectionFailureException | RedisSystemException e) {
			log.error("Redis 조회수 증가 실패, sharedNoteId={}", sharedNoteId, e);
		}
	}

	private boolean isDuplicate(long memberId, long sharedNoteId) {
		try {
			return Boolean.TRUE.equals(redisTemplate.hasKey(RedisKeyFactory.viewHistoryKey(sharedNoteId, memberId)));
		} catch (RedisConnectionFailureException | RedisSystemException e) {
			log.error("Redis 장애로 조회 기록 확인 실패. sharedNoteId={}, memberId={}", sharedNoteId, memberId, e);
			return false;
		}
	}

	private Long getRedisViewCount(long sharedNoteId) {
		try {
			Object value = redisTemplate.opsForValue().get(RedisKeyFactory.viewCountKey(sharedNoteId));
			return value == null ? 0L : Long.parseLong(value.toString());
		} catch (RedisConnectionFailureException | RedisSystemException e) {
			log.error("Redis 장애 발생, 기본값 반환 sharedNoteID={}", sharedNoteId, e);
			return 0L;
		}
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
			this::getTotalViewCount
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
				return getUserOwnedSharedNotes(member, );
			}
			default -> throw new SharedNoteHandler(ErrorStatus.SHAREDNOTE_TYPE_ERROR);
		}
	}

	private UserSharedNotesGetResponse getUserOwnedSharedNotes(Member member) {

		usedPencilFinder.findAllByMemberOrderByCreatedAtDesc()
		Set<Long> likedNoteIds = new HashSet<>(likedNoteFinder.findLikedSharedNoteIds(member, sharedNotes));
		return UserSharedNotesGetResponse.ofOwned(sharedNotes, likedNoteIds, viewcountMap);
	}

	private UserSharedNotesGetResponse getUserSharedNotes(Member member, NoteType noteType,
		LimjangPropertyType propertyType, LimjangPriceType priceType, String keyword) {
		List<SharedNote> sharedNotes = sharedNoteFinder.findUserSharedNotes(member, noteType, propertyType, priceType, keyword);
		Map<Long, Long> viewcountMap = mapIdsAndViewcount(sharedNotes);

		Set<Long> likedNoteIds = new HashSet<>(likedNoteFinder.findLikedSharedNoteIds(member, sharedNotes));
		return UserSharedNotesGetResponse.ofShared(sharedNotes, likedNoteIds, viewcountMap);
	}

	private UserSharedNotesGetResponse getUserLikedSharedNotes(Member member, LimjangPropertyType propertyType,
		LimjangPriceType priceType, String keyword) {

		List<LikedNote> userLikedNotes = likedNoteFinder.findAllByMemberAndDynamic(member, propertyType,
			priceType, keyword);
		List<SharedNote> sharedNotes = userLikedNotes.stream().map(LikedNote::getSharedNote).toList();

		Set<Long> purchasedIds = new HashSet<>(usedPencilFinder.findByMemberInSharedNoteIdsAndTypeIsOwned(member, userLikedNotes.stream().map(it -> it.getSharedNote().getSharedNoteId()).toList()));
		Map<Long, Long> viewcountMap = mapIdsAndViewcount(sharedNotes);

		return UserSharedNotesGetResponse.ofLiked(sharedNotes, purchasedIds, viewcountMap);
	}
}
