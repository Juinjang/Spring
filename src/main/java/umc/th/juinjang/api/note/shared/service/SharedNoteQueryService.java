package umc.th.juinjang.api.note.shared.service;

import java.time.Duration;

import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.api.note.liked.service.LikedNoteFinder;
import umc.th.juinjang.api.pencil.service.UsedPencilFinder;
import umc.th.juinjang.api.note.shared.service.response.SharedNoteGetResponse;
import umc.th.juinjang.common.redis.RedisKeyFactory;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.note.shared.model.SharedNote;

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

		long viewCount = sharedNote.getViewCount() + getViewCount(sharedNoteId);
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

	private Long getViewCount(long sharedNoteId) {
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
}
