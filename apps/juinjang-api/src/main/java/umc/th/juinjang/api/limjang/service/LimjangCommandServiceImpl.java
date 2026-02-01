package umc.th.juinjang.api.limjang.service;

import static umc.th.juinjang.api.limjang.service.LimjangPriceBridge.determineLimjangPrice;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.LimjangHandler;
import umc.th.juinjang.common.exception.handler.MemberHandler;
import umc.th.juinjang.api.limjang.controller.request.LimjangPatchRequest;
import umc.th.juinjang.api.limjang.controller.request.LimjangPostRequest;
import umc.th.juinjang.api.limjang.controller.request.LimjangsDeleteRequest;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.model.LimjangPrice;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.repository.LimjangRepository;
import umc.th.juinjang.domain.member.repository.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class LimjangCommandServiceImpl implements LimjangCommandService {

  private final LimjangRepository limjangRepository;
  private final MemberRepository memberRepository;

  @Override
  @Transactional
  public Limjang postLimjang(LimjangPostRequest postDto, Member member) {
    Limjang limjang = postDto.toEntity();
    limjang.saveMemberAndPrice(getMemberById(member), getLimjangPrice(postDto.price(), postDto.purposeType(), postDto.priceType()));
    return limjangRepository.save(limjang);
  }

  private Member getMemberById(Member member) {
    return memberRepository.findById(member.getMemberId())
        .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
  }

  @Override
  @Transactional
  public void deleteLimjangs(LimjangsDeleteRequest requestIds, Member member) {
    List<Long> ids = requestIds.limjangIdList();
    checkLimjangsExistence(ids, member);
    limjangRepository.softDeleteByIds(ids);
  }

  private void checkLimjangsExistence(List<Long> ids, Member member) {
    if (isRequestSizeMismatch(ids, limjangRepository.findAllByLimjangIdInAndMemberIdAndDeletedIsFalse(ids, member))) {
      throw new LimjangHandler(ErrorStatus.LIMJANG_NOTFOUND_ERROR);
    }
  }

  private boolean isRequestSizeMismatch(List<Long> ids, List<Limjang> findLimjangs) {
    return ids.size() != findLimjangs.size();
  }

  @Override
  @Transactional
  public void updateLimjang(Member member, long id, LimjangPatchRequest request) {
    Limjang limjang = getByMemberAndIdWithLimjangPrice(member, id);
    limjang.updateLimjang(request.address(), request.addressDetail(), request.nickname(), LimjangPriceType.find(request.priceType()));
    limjang.getLimjangPrice().updateLimjangPrice(getLimjangPrice(request.priceList(), limjang.getPurpose().getValue(), request.priceType()));
  }

  private LimjangPrice getLimjangPrice(List<String> priceList, int purpose, int priceType) {
    return determineLimjangPrice(priceList, purpose, priceType);
  }

  private Limjang getByMemberAndIdWithLimjangPrice(Member member, long id) {
    return limjangRepository.findByLimjangIdAndMemberIdWithLimjangPriceAndDeletedIsFalse(id, member)
        .orElseThrow(() -> new LimjangHandler(ErrorStatus.LIMJANG_NOTFOUND_ERROR));
  }
}