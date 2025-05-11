package umc.th.juinjang.api.reward.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.reward.model.RewardType;
import umc.th.juinjang.domain.reward.repository.RewardRepository;

@Component
@RequiredArgsConstructor
public class RewardFinder {

	private final RewardRepository rewardRepository;

	boolean existsByTypeAndMilestoneAndSharedNoteId(RewardType type, Long milestone, Long sharedNoteId) {
		return rewardRepository.existsByTypeAndMilestoneAndSharedNoteId(type, milestone, sharedNoteId);
	}
}
