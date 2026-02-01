package umc.th.juinjang.api.limjang.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.LimjangHandler;
import umc.th.juinjang.common.exception.handler.MemberHandler;
import umc.th.juinjang.api.limjang.service.response.LimjangDetailGetResponse;
import umc.th.juinjang.api.limjang.service.response.LimjangsGetByKeywordResponse;
import umc.th.juinjang.api.limjang.service.response.LimjangsGetResponse;
import umc.th.juinjang.api.limjang.service.response.LimjangsMainGetResponse;
import umc.th.juinjang.api.limjang.service.response.LimjangsMainGetVersion2Response;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.api.limjang.controller.parameter.LimjangSortOptions;
import umc.th.juinjang.domain.limjang.repository.LimjangRepository;
import umc.th.juinjang.domain.member.repository.MemberRepository;
import umc.th.juinjang.domain.scrap.repository.ScrapRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class LimjangQueryServiceImpl implements LimjangQueryService{

  private final LimjangRepository limjangRepository;
  private final MemberRepository memberRepository;
  private final ScrapRepository scrapRepository;

  @Override
  @Transactional(readOnly = true)
  public LimjangsGetResponse getLimjangTotalList(Member member, LimjangSortOptions sort) {
    checkMemberExist(member);
    List<Limjang> limjangList = limjangRepository.findAllByMemberAndDeletedIsFalseOrderByParam(member, sort);
    return LimjangsGetResponse.of(limjangList, mapLimjangToScrapStatus(limjangList));
  }

  @Override
  @Transactional(readOnly = true)
  public LimjangsMainGetResponse getLimjangsMain(final Member member) {
    return LimjangsMainGetResponse.of(limjangRepository.findAllByMemberAndDeletedIsFalseWithReportAndLimjangPriceOrderByUpdateAtLimit5(checkMemberExist(member)));
  }

  @Override
  @Transactional(readOnly = true)
  public LimjangsGetByKeywordResponse getLimjangSearchList(Member member, String keyword) {
    checkMemberExist(member);
    List<Limjang> limjangList = limjangRepository.searchLimjangsWhereDeletedIsFalse(member, keyword);
    return LimjangsGetByKeywordResponse.of(limjangList, mapLimjangToScrapStatus(limjangList));
  }

  @Override
  @Transactional(readOnly = true)
  public LimjangDetailGetResponse getDetail(long id, Member member) {
    return LimjangDetailGetResponse.of(getByIdAndMember(id, member));
  }

  private Limjang getByIdAndMember(Long id, Member member) {
    return limjangRepository.findByLimjangIdAndMemberAndDeletedIsFalse(id, member).orElseThrow(() -> new LimjangHandler(ErrorStatus.LIMJANG_NOTFOUND_ERROR));
  }

  @Override
  @Transactional(readOnly = true)
  public LimjangsMainGetVersion2Response getLimjangsMainVersion2(Member member) {
    return LimjangsMainGetVersion2Response.of(limjangRepository.findAllByMemberAndDeletedIsFalseWithReportAndLimjangPriceOrderByUpdateAtLimit5(checkMemberExist(member)));
  }

  private Member checkMemberExist(Member member) {
    return memberRepository.findById(member.getMemberId()).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
  }

  private Map<Long, Boolean> mapLimjangToScrapStatus(List<Limjang> limjangList) {
    Set<Long> limjangIdsInScrap = getLimjangIdsInScrap(limjangList);
    return limjangList.stream().collect(Collectors.toMap(
        Limjang::getLimjangId,
        it -> limjangIdsInScrap.contains(it.getLimjangId())
    ));
  }

  private Set<Long> getLimjangIdsInScrap(List<Limjang> limjangList) {
    return new HashSet<>(scrapRepository.findAllByLimjangIdIn(limjangList).stream().map(it -> it.getLimjangId().getLimjangId()).toList());
  }
}
