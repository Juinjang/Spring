package umc.th.juinjang.api.limjang.service;

import umc.th.juinjang.api.limjang.service.response.LimjangDetailGetResponse;

import umc.th.juinjang.api.limjang.controller.parameter.LimjangSortOptions;
import umc.th.juinjang.api.limjang.service.response.LimjangsGetByKeywordResponse;
import umc.th.juinjang.api.limjang.service.response.LimjangsGetResponse;
import umc.th.juinjang.api.limjang.service.response.LimjangsMainGetResponse;
import umc.th.juinjang.api.limjang.service.response.LimjangsMainGetVersion2Response;
import umc.th.juinjang.domain.member.model.Member;

public interface LimjangQueryService {

  LimjangsGetResponse getLimjangTotalList(Member member, LimjangSortOptions sort);

  LimjangsMainGetResponse getLimjangsMain(Member member);

  LimjangsGetByKeywordResponse getLimjangSearchList(Member member, String keyword);

  LimjangDetailGetResponse getDetail(long id, Member member);

  LimjangsMainGetVersion2Response getLimjangsMainVersion2(Member member);
}
