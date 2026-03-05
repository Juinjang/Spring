package umc.th.juinjang.domain.limjang.repository;

import java.util.List;

import umc.th.juinjang.api.limjang.controller.parameter.LimjangSortOptions;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.member.model.Member;

public interface LimjangQueryDslRepository {

	List<Limjang> searchLimjangsWhereDeletedIsFalse(Member member, String keyword);

	List<Limjang> findAllByMemberAndDeletedIsFalseWithReportAndLimjangPriceOrderByUpdateAtLimit5(Member member);

	List<Limjang> findAllByMemberAndDeletedIsFalseOrderByParam(Member member, LimjangSortOptions sort);

	List<Limjang> findAllByMemberAndDeletedIsFalseOrderByParamV2(Member member, LimjangSortOptions sort,
		String keyword);
}
