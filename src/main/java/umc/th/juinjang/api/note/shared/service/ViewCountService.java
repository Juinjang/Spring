package umc.th.juinjang.api.note.shared.service;

import java.time.Duration;

import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.common.redis.RedisKeyFactory;

@Component
@RequiredArgsConstructor
@Slf4j
public class ViewCountService {

	private final RedisTemplate<String, String> redisTemplate;
	private final SharedNoteFinder sharedNoteFinder;

	public void recordViewerHistory(long memberId, long sharedNoteId) {
		try {
			redisTemplate.opsForValue()
				.setIfAbsent(RedisKeyFactory.viewHistoryKey(sharedNoteId, memberId), "1", Duration.ofHours(3));
		} catch (RedisConnectionFailureException | RedisSystemException e) {
			log.error("Redis 연결 실패 - 중복 조회 기록 불가, sharedNoteId={}, memberId={}", sharedNoteId, memberId, e);
		}
	}

	public void increaseViewCount(long sharedNoteId) {
		try {
			redisTemplate.opsForValue().increment(RedisKeyFactory.viewCountKey(sharedNoteId));
		} catch (RedisConnectionFailureException | RedisSystemException e) {
			log.error("Redis 조회수 증가 실패, sharedNoteId={}", sharedNoteId, e);
		}
	}

	public boolean isDuplicate(long memberId, long sharedNoteId) {
		try {
			return Boolean.TRUE.equals(redisTemplate.hasKey(RedisKeyFactory.viewHistoryKey(sharedNoteId, memberId)));
		} catch (RedisConnectionFailureException | RedisSystemException e) {
			log.error("Redis 장애로 조회 기록 확인 실패. sharedNoteId={}, memberId={}", sharedNoteId, memberId, e);
			return false;
		}
	}

	public Long getRedisViewCount(long sharedNoteId) {

		String key = RedisKeyFactory.viewCountKey(sharedNoteId);

		try {
			Object value = redisTemplate.opsForValue().get(key);

			if (value == null) {
				Long viewCountFromDb = sharedNoteFinder.findViewCountById(sharedNoteId);
				redisTemplate.opsForValue().set(key, viewCountFromDb.toString());
				return viewCountFromDb;
			}

			return Long.parseLong(value.toString());
		} catch (RedisConnectionFailureException | RedisSystemException e) {
			log.error("Redis 장애 발생, 기본값 반환 sharedNoteID={}", sharedNoteId, e);
			return 0L;
		}
	}
}
