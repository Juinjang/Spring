package umc.th.juinjang.api.sharednote.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.pencil.service.UsedPencilUpdater;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.SharedNoteHandler;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.note.shared.model.SharedNote;
import umc.th.juinjang.domain.pencil.used.model.UsedPencil;
import umc.th.juinjang.domain.pencil.used.model.Usedtype;

@Service
@RequiredArgsConstructor
public class SharedNoteCommandService {

	private final SharedNoteFinder sharedNoteFinder;
	private final UsedPencilUpdater usedPencilUpdater;

	public void createSharedNotePurchase(Member member, Long sharedNoteId) {
		SharedNote sharedNote = sharedNoteFinder.findById(sharedNoteId);
		checkOwnedPencil(sharedNote);
		try {
			usedPencilUpdater.save(createUsedPencil(member, sharedNoteId, sharedNote));
		} catch (DataIntegrityViolationException e) {
			// 중복 결제를 방지하기 위해 member 아이디 + sharedNoteId 유니크 제약 조건 걸어도 될듯?
			throw new SharedNoteHandler(ErrorStatus.SHAREDNOTE_CONFLICT);
		}
	}

	private void checkOwnedPencil(SharedNote sharedNote) {
		int usersPencil = 1;
		if (usersPencil - sharedNote.getPrice() < 0) {
			throw new SharedNoteHandler(ErrorStatus.SHAREDNOTE_ALREADY_PURCHASE);
		}
	}

	private UsedPencil createUsedPencil(Member member, Long sharedNoteId, SharedNote sharedNote) {
		return UsedPencil.create(member, sharedNoteId, sharedNote.getPrice(), Usedtype.OWNED,
			sharedNote.getBuildingName(), 0L);
	}
}
