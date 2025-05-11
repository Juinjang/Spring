package umc.th.juinjang.domain.note.shared.model;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ViewCountPolicy {

	private final Map<Long, Long> milestoneRewardMap = Map.of(
		10L, 1L,
		25L, 2L,
		50L, 3L
	);

	private final Map<Long, String> milestoneMessageMap = Map.of(
		10L, "조회수 10회 달성!",
		25L, "조회수 25회 달성!",
		50L, "조회수 50회 달성!"
	);

	public Long getRewardForExactMilestone(long viewCount) {
		return milestoneRewardMap.get(viewCount);
	}

	public String getMessageForMilestone(long milestone) {
		String message = milestoneMessageMap.get(milestone);
		if (message == null) {
			log.info("정의되지 않은 milestone입니다: milestoen={}", milestone);
		}
		return message;
	}
}
