package umc.th.juinjang.api.reward.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.api.pencil.service.AcquiredPencilUpdater;
import umc.th.juinjang.api.pencilAccount.service.PencilAccountFinder;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.note.shared.model.ViewCountPolicy;
import umc.th.juinjang.domain.pencil.acquired.AcquiredPencil;
import umc.th.juinjang.domain.pencil.acquired.AcquiredType;
import umc.th.juinjang.domain.pencilaccount.model.PencilAccount;
import umc.th.juinjang.domain.reward.model.Reward;
import umc.th.juinjang.domain.reward.model.RewardType;

@Component
@RequiredArgsConstructor
@Slf4j
public class RewardService {

	private final AcquiredPencilUpdater acquiredPencilUpdater;
	private final PencilAccountFinder pencilAccountFinder;
	private final ViewCountPolicy viewCountPolicy;
	private final RewardFinder rewardFinder;
	private final RewardUpdater rewardUpdater;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void giveViewCountReward(Member member, Long sharedNoteId, Long milestone, Long rewardPencil) {

		if (alreadyViewCountRewardEarned(RewardType.VIEWCOUNT, sharedNoteId, milestone)) {
			return;
		}

		PencilAccount account = pencilAccountFinder.findByMemberWithLock(member);
		account.increaseAcquiredBalance(rewardPencil);

		acquiredPencilUpdater.save(
			createAcquiredPencil(member, sharedNoteId, milestone, rewardPencil));
		rewardUpdater.save(createReward(member, sharedNoteId, milestone, rewardPencil));
		
		log.info("유저에게 조회수 리워드 지급 완료: memberId={}, sharedNoteId={}, milestone={}, rewardPencil={}",
			member.getMemberId(), sharedNoteId, milestone, rewardPencil);
	}

	private AcquiredPencil createAcquiredPencil(Member member, Long sharedNoteId, Long milestone, Long rewardPencil) {
		return AcquiredPencil.create(member.getMemberId(), viewCountPolicy.getMessageForMilestone(milestone), sharedNoteId,
			rewardPencil, AcquiredType.VIEWCOUNT);
	}

	private Reward createReward(Member member, Long sharedNoteId, Long milestone, Long rewardPencil) {
		return Reward.create(member, RewardType.VIEWCOUNT, milestone, sharedNoteId, rewardPencil);
	}

	private boolean alreadyViewCountRewardEarned(RewardType type, Long sharedNoteId, Long milestone) {
		return rewardFinder.existsByTypeAndMilestoneAndSharedNoteId(type, milestone, sharedNoteId);
	}
}
