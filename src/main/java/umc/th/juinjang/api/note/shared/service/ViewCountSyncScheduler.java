package umc.th.juinjang.api.note.shared.service;

import static umc.th.juinjang.common.redis.RedisKeyFactory.*;

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

	// @Scheduled(cron = "0 0 */6 * * *") // 6시간마다 실행
	@Scheduled(cron = "*/30 * * * * *")
	@Transactional
	public void syncRedisViewCountsToRDB() {
		Set<String> keys = redisTemplate.keys(VIEW_COUNT + "*");
		if (keys == null || keys.isEmpty())
			return;

		log.info("Redis RDB 조회수 동기화 시작: 총 {}개", keys.size());

		for (String key : keys) {
			try {
				long sharedNoteId = Long.parseLong(key.split(":")[2]);
				Object value = redisTemplate.opsForValue().get(key);
				if (value == null) {
					log.warn("조회수 값 없음 - key={}", key);
					continue;
				}
				long addAmount = Long.parseLong(value.toString());
				sharedNoteUpdater.updateViewCount(sharedNoteId, addAmount);
				log.info("조회수 동기화: sharedNoteId={}, Redis={}", sharedNoteId, addAmount);
				redisTemplate.delete(key);
			} catch (Exception e) {
				log.error("동기화 실패: key={}, error={}", key, e.getMessage(), e);
			}
		}
	}
}
