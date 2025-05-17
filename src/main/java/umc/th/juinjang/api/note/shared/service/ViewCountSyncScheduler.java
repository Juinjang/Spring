package umc.th.juinjang.api.note.shared.service;

import static umc.th.juinjang.common.redis.RedisKeyFactory.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class ViewCountSyncScheduler {

	private final RedisTemplate<String, Object> redisTemplate;
	private final SharedNoteUpdater sharedNoteUpdater;
	private final SharedNoteFinder sharedNoteFinder;

	@Scheduled(cron = "0 0 */6 * * *")
	@Transactional
	public void syncRedisViewCountsToRDB() {
		Set<String> keys = redisTemplate.keys(VIEW_COUNT + "*");
		if (keys == null || keys.isEmpty())
			return;

		List<Long> sharedNoteIds = getSharedNoteIdsInRedis(keys);
		Map<Long, Long> dbViewCounts = sharedNoteFinder.findAllIdAndViewCountById(sharedNoteIds);

		for (String key : keys) {
			try {
				long sharedNoteId = Long.parseLong(key.split(":")[2]);
				Object value = redisTemplate.opsForValue().get(key);
				if (value == null) {
					log.warn("조회수 값 없음 - key={}", key);
					continue;
				}

				long redisViewCount = Long.parseLong(value.toString());
				long dbViewCount = dbViewCounts.getOrDefault(sharedNoteId, 0L);

				if (redisViewCount > dbViewCount) {
					sharedNoteUpdater.updateViewCount(sharedNoteId, redisViewCount);
					log.info("조회수 동기화: sharedNoteId={}, Redis={}, DB={}", sharedNoteId, redisViewCount,
						dbViewCount);
				} else {
					log.info("동기화 생략: sharedNoteId={}, Redis={}, DB={}", sharedNoteId, redisViewCount,
						dbViewCount);
				}
			} catch (Exception e) {
				log.error("동기화 실패: key={}, error={}", key, e.getMessage(), e);
			}
		}
	}

	private List<Long> getSharedNoteIdsInRedis(Set<String> keys) {
		return keys.stream()
			.map(k -> {
				try {
					return Long.parseLong(k.split(":")[2]);
				} catch (Exception e) {
					log.warn("잘못된 키 형식: {}", k);
					return null;
				}
			})
			.filter(Objects::nonNull)
			.toList();
	}
}
