package umc.th.juinjang.api.note.shared.service;

import java.time.Duration;

import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.common.redis.RedisKeyFactory;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.note.shared.model.SharedNote;
import umc.th.juinjang.event.publisher.ApplicationRewardViewCountPublisherAdapter;

@Component
@RequiredArgsConstructor
@Slf4j
public class ViewCountService {

	private final RedisTemplate<String, String> redisTemplate;
	private final SharedNoteFinder sharedNoteFinder;
	private final SharedNoteUpdater sharedNoteUpdater;
	private final ApplicationRewardViewCountPublisherAdapter applicationRewardViewCountPublisherAdapter;

	public void recordViewerHistory(long memberId, long sharedNoteId) {
		try {
			redisTemplate.opsForValue()
				.setIfAbsent(RedisKeyFactory.viewHistoryKey(sharedNoteId, memberId), "1", Duration.ofHours(3));
		} catch (RedisConnectionFailureException | RedisSystemException e) {
			log.error("Redis 연결 실패 - 중복 조회 기록 불가, sharedNoteId={}, memberId={}", sharedNoteId, memberId, e);
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

	public long getViewCount(Member member, SharedNote sharedNote) {
		long sharedNoteId = sharedNote.getSharedNoteId();
		long viewCount = sharedNoteFinder.findViewCountById(sharedNoteId);

		if (!isDuplicate(member.getMemberId(), sharedNoteId) && sharedNote.getDeletedAt() == null) {
			sharedNoteUpdater.updateViewCount(sharedNoteId);
			viewCount++;
			recordViewerHistory(member.getMemberId(), sharedNoteId);

			applicationRewardViewCountPublisherAdapter.checkViewCountRewardPolicy(sharedNote.getMember(),
				sharedNote.getSharedNoteId(), viewCount);
		}
		return viewCount;
	}
}
