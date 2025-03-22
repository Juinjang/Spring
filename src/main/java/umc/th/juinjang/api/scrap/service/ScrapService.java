package umc.th.juinjang.api.scrap.service;

import umc.th.juinjang.domain.member.model.Member;

public interface ScrapService {

  void createScrap(Member member, long limjangId);

  void deleteScrap(Member member, long limjangId);
}
