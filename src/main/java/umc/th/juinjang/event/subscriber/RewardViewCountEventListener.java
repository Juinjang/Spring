package umc.th.juinjang.event.subscriber;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.api.reward.service.RewardService;
import umc.th.juinjang.domain.note.shared.model.ViewCountPolicy;
import umc.th.juinjang.event.RewardViewCountEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class RewardViewCountEventListener {

	private final ViewCountPolicy viewCountPolicy;
	private final RewardService rewardService;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleRewardViewCountEvent(RewardViewCountEvent rewardViewCountEvent) {

		Long reward = viewCountPolicy.getRewardForExactMilestone(
			rewardViewCountEvent.viewCount());

		if (reward == null) {
			return;
		}

		try {
			rewardService.giveViewCountReward(rewardViewCountEvent.member(), rewardViewCountEvent.sharedNoteId(),
				rewardViewCountEvent.viewCount(), reward);
		} catch (Exception e) {
			log.error("조회수 리워드 지급 실패", e);
		}
	}
}
