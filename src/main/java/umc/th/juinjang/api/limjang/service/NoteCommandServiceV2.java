package umc.th.juinjang.api.limjang.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.address.service.AddressUpdater;
import umc.th.juinjang.api.limjang.controller.request.NoteInitRequest;
import umc.th.juinjang.api.limjang.controller.request.NotePatchRequest;
import umc.th.juinjang.api.limjang.controller.request.NotePostRequest;
import umc.th.juinjang.api.limjang.service.response.NotePostResponse;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.LimjangHandler;
import umc.th.juinjang.domain.limjang.model.Address;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.model.LimjangPrice;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPurpose;
import umc.th.juinjang.domain.member.model.Member;

@Service
@RequiredArgsConstructor
public class NoteCommandServiceV2 {

	private final AddressUpdater addressUpdater;
	private final NoteUpdater noteUpdater;
	private final NotePriceUpdater notePriceUpdater;
	private final NoteFinder noteFinder;

	@Transactional
	public NotePostResponse createNote(NotePostRequest request, Member member) {
		Limjang note = request.toEntity(member);

		validatePriceType(request.purposeType(), request.priceType());

		notePriceUpdater.save(note.getLimjangPrice());
		addressUpdater.save(note.getAddressEntity());
		Limjang savedNote = noteUpdater.save(note);
		return NotePostResponse.of(savedNote.getLimjangId());
	}

	@Transactional
	public void updateNote(Long noteId, NotePatchRequest request) {
		Limjang note = noteFinder.getNoteByIdWithAddressAndNotePriceWhereDeletedIsFalse(noteId);

		validatePriceType(note.getPurpose(), request.priceType());

		LimjangPrice newPrice = request.toUpdatedPrice(note.getPurpose());
		Address newAddress = request.toUpdatedAddress();

		note.getAddressEntity().update(newAddress);
		note.getLimjangPrice().updateLimjangPrice(newPrice);
		note.updateNote(request.nickname(), request.priceType(), request.floor(), request.pyong());
	}

	@Transactional
	public void updateNoteV2(Long noteId, NotePatchRequest request) {
		Limjang note = noteFinder.getNoteByIdWhereDeletedIsFalse(noteId);

		validatePriceType(note.getPurpose(), request.priceType());

		LimjangPrice newPrice = request.toUpdatedPrice(note.getPurpose());
		Address newAddress = request.toUpdatedAddress();

		note.getAddressEntity().update(newAddress);
		note.getLimjangPrice().updateLimjangPrice(newPrice);
		note.updateNote(request.nickname(), request.priceType(), request.floor(), request.pyong());
	}

	private void validatePriceType(LimjangPurpose purposeType, LimjangPriceType priceType) {
		if (
			(purposeType == LimjangPurpose.RESIDENTIAL_PURPOSE && priceType == LimjangPriceType.MARKET_PRICE) ||
				(purposeType == LimjangPurpose.INVESTMENT && priceType != LimjangPriceType.MARKET_PRICE)
		) {
			throw new LimjangHandler(ErrorStatus.LIMJANG_POST_TYPE_ERROR);
		}
	}

	public NotePostResponse initNote(@Valid NoteInitRequest request, Member member) {
		Limjang note = request.toEntity(member);
		validatePriceType(request.purposeType(), request.priceType());

		notePriceUpdater.save(note.getLimjangPrice());
		Limjang savedNote = noteUpdater.save(note);

		return NotePostResponse.of(savedNote.getLimjangId());
	}
}
