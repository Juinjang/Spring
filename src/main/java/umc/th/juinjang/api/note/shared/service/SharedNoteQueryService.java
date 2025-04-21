package umc.th.juinjang.api.note.shared.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.note.liked.service.LikedNoteFinder;
import umc.th.juinjang.api.pencil.service.UsedPencilFinder;
import umc.th.juinjang.api.note.shared.service.response.SharedNoteGetResponse;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.note.shared.model.SharedNote;

@Service
@RequiredArgsConstructor
public class SharedNoteQueryService {

	private final UsedPencilFinder usedPencilFinder;
	private final SharedNoteFinder sharedNoteFinder;
	private final LikedNoteFinder likedNoteFinder;

	@Transactional(readOnly = true)
	public SharedNoteGetResponse findSharedNote(Member member, Long sharedNoteId) {
		// 노트 있는지 확인
		SharedNote sharedNote = sharedNoteFinder.findById(sharedNoteId);
		Limjang limjang = sharedNote.getLimjang();

		// 소유했는지 판단
		boolean isBuyer = usedPencilFinder.existsByMemberAndSharedNoteId(member, sharedNoteId);
		int viewCount = 0;

		Integer countBuyer = makeBuyerCount(usedPencilFinder.countBySharedNoteId(sharedNoteId));
		boolean isLiked = likedNoteFinder.existsByMemberAndSharedNote(member, sharedNote);

		if (isBuyer) {
			return SharedNoteGetResponse.ofPurchased(true, limjang, limjang.getAddressEntity(), sharedNote,
				sharedNote.getMember(), countBuyer, isLiked, viewCount);
		}
		return SharedNoteGetResponse.ofNotPurchased(false, limjang, limjang.getAddressEntity(), sharedNote,
			sharedNote.getMember(), countBuyer, isLiked, viewCount);
	}

	private Integer makeBuyerCount(int count) {
		if (count >= 100) {
			return 100;
		} else if (count >= 50) {
			return 50;
		} else if (count >= 30) {
			return 30;
		} else if (count >= 10) {
			return 10;
		}
		return null;
	}
}
