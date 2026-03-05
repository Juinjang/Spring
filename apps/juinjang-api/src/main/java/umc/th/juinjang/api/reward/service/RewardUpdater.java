package umc.th.juinjang.api.reward.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.reward.model.Reward;
import umc.th.juinjang.domain.reward.repository.RewardRepository;

@Component
@RequiredArgsConstructor
public class RewardUpdater {

	private final RewardRepository rewardRepository;

	void save(Reward reward) {
		rewardRepository.save(reward);
	}
}