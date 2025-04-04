package umc.th.juinjang.api.limjang.service;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.limjang.controller.parameter.LimjangSortOptions;
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
}
