package umc.th.juinjang.api.reward.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.pencil.service.AcquiredPencilUpdater;
import umc.th.juinjang.api.pencilAccount.service.PencilAccountFinder;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.note.shared.model.ViewCountPolicy;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredPencil;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredType;
import umc.th.juinjang.domain.pencilaccount.model.PencilAccount;
import umc.th.juinjang.domain.reward.model.Reward;
import umc.th.juinjang.domain.reward.model.RewardType;

@Component
@RequiredArgsConstructor
public class RewardService {

	private final AcquiredPencilUpdater acquiredPencilUpdater;
	private final PencilAccountFinder pencilAccountFinder;
	private final ViewCountPolicy viewCountPolicy;
	private final RewardFinder rewardFinder;
	private final RewardUpdater rewardUpdater;

	@Transactional
	public void giveViewCountReward(Member member, Long sharedNoteId, Long milestone, Long rewardPencil) {

		if (alreadyViewCountRewardEarned(RewardType.VIEWCOUNT, sharedNoteId, milestone)) {
			return;
		}

		PencilAccount account = pencilAccountFinder.findByMemberWithLock(member);
		account.increaseAcquiredBalance(rewardPencil);

		acquiredPencilUpdater.save(
			createAcquiredPencil(member, sharedNoteId, milestone, rewardPencil));
		rewardUpdater.save(createReward(member, sharedNoteId, milestone, rewardPencil));
	}

	private AcquiredPencil createAcquiredPencil(Member member, Long sharedNoteId, Long milestone, Long rewardPencil) {
		PencilAccount account = pencilAccountFinder.findByMember(member);
		return AcquiredPencil.create(member, viewCountPolicy.getMessageForMilestone(milestone), sharedNoteId,
			rewardPencil, account.getTotalBalance(), false, AcquiredType.VIEWCOUNT);
	}

	private Reward createReward(Member member, Long sharedNoteId, Long milestone, Long rewardPencil) {
		return Reward.create(member, RewardType.VIEWCOUNT, milestone, sharedNoteId, rewardPencil);
	}

	private boolean alreadyViewCountRewardEarned(RewardType type, Long sharedNoteId, Long milestone) {
		return rewardFinder.existsByTypeAndMilestoneAndSharedNoteId(type, milestone, sharedNoteId);
	}
}
