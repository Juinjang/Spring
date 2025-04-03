package umc.th.juinjang.api.limjang.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.LimjangHandler;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.repository.LimjangRepository;

@Component
@RequiredArgsConstructor
public class NoteFinder {

	private final LimjangRepository limjangRepository;

	protected Limjang getNoteByIdWhereDeletedIsFalse(long id) {
		return limjangRepository.findByLimjangIdAndDeletedIsFalse(id)
			.orElseThrow(() -> new LimjangHandler(ErrorStatus.LIMJANG_NOTFOUND_ERROR));
	}

	protected Limjang getNoteByIdWithAddressAndNotePriceWhereDeletedIsFalse(long id) {
		return limjangRepository.findNoteByIdWithAddressAndNotePriceWhereDeletedIsFalse(id)
			.orElseThrow(() -> new LimjangHandler(ErrorStatus.LIMJANG_NOTFOUND_ERROR));
	}
}
