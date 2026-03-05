package umc.th.juinjang.domain.reward.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import umc.th.juinjang.domain.reward.model.Reward;
import umc.th.juinjang.domain.reward.model.RewardType;

public interface RewardRepository extends JpaRepository<Reward, Long> {

	boolean existsByTypeAndMilestoneAndSharedNoteId(RewardType type, Long milestone, Long sharedNoteId);
}
