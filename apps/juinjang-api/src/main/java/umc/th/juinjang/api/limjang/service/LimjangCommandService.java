package umc.th.juinjang.api.limjang.service;

import umc.th.juinjang.api.limjang.controller.request.LimjangPatchRequest;
import umc.th.juinjang.api.limjang.controller.request.LimjangPostRequest;
import umc.th.juinjang.api.limjang.controller.request.LimjangsDeleteRequest;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.member.model.Member;

public interface LimjangCommandService {

  Limjang postLimjang(LimjangPostRequest request, Member member);

  void deleteLimjangs(LimjangsDeleteRequest deleteIds, Member member);

  void updateLimjang(Member member, long limjangId, LimjangPatchRequest request);
}
