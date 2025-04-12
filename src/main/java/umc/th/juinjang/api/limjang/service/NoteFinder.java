package umc.th.juinjang.api.limjang.service;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.limjang.controller.parameter.LimjangSortOptions;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.LimjangHandler;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.repository.LimjangRepository;
import umc.th.juinjang.domain.member.model.Member;

@Component
@RequiredArgsConstructor
public class NoteFinder {

	private final LimjangRepository limjangRepository;

	protected List<Limjang> findAllByMemberOrderByOptions(Member member, LimjangSortOptions sortOptions) {
		return limjangRepository.findAllByMemberAndDeletedIsFalseOrderByParamV2(member, sortOptions);
	}

	protected Limjang getNoteByIdWhereDeletedIsFalse(long id) {
		return limjangRepository.findByLimjangIdAndDeletedIsFalse(id)
			.orElseThrow(() -> new LimjangHandler(ErrorStatus.LIMJANG_NOTFOUND_ERROR));
	}

	protected Limjang getNoteByIdWithAddressAndNotePriceWhereDeletedIsFalse(long id) {
		return limjangRepository.findByIdWithAddressAndNotePriceWhereDeletedIsFalse(id)
			.orElseThrow(() -> new LimjangHandler(ErrorStatus.LIMJANG_NOTFOUND_ERROR));
	}

	protected List<Limjang> getAllByMemberWithAddressAndNotePriceWhereRewardPencilIsNotNullAndDeletedIsFalse(
		Member member) {
		return limjangRepository.findAllByMemberWithAddressAndNotePriceWhereRewardPencilIsNotNullAndDeletedIsFalse(
			member);
	}
}
