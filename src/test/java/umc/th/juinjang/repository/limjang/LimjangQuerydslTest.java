package umc.th.juinjang.repository.limjang;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static umc.th.juinjang.repository.limjang.LimjangFixture.FIXTURE_MEMBER;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import umc.th.juinjang.config.TestConfig;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.repository.LimjangRepository;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.limjang.model.LimjangPurpose;
import umc.th.juinjang.domain.member.repository.MemberRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestConfig.class)
// @Rollback(value = false)
public class LimjangQuerydslTest {

  @Autowired
  private LimjangRepository limjangRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Nested
  class testSerach {

    private Member member;

    @BeforeEach
    void setUp() {
      member = FIXTURE_MEMBER;
      memberRepository.save(member);
    }

    @Test
    @DisplayName("키워드를 전달하면 멤버가 소유한 게시글 중 닉네임, 주소, 상세주소 컬럼중 하나라도 키워드를 포함하는 게시글을 리턴한다.")
    void testIncludeKeyword() {

      // given
      Limjang limjang1 = createLimjang(member, "경기도 구리시 인창동", "삼성아파트", "우리 집");
      Limjang limjang2 = createLimjang(member, "경기도 구리시", "인창", "우리 집");
      Limjang limjang3 = createLimjang(member, "경기도 구리시", "어쩌구", "인창");
      limjangRepository.saveAll(List.of(limjang1, limjang2, limjang3));

      // when
      String keyword = "인창";
      List<Limjang> findLimjangs = limjangRepository.searchLimjangsWhereDeletedIsFalse(member, keyword);
      // then

      for (int i = 0; i < findLimjangs.size(); i++) {
        System.out.println(findLimjangs.get(i).getLimjangId());
      }
      assertThat(findLimjangs)
          .hasSize(3);
    }

    @Test
    @DisplayName("주소, 상세주소, 닉네임 중 하나라도 키워드를 포함하지 않는 게시글은 검색에 걸리지 않음")
    void testNotIncludeKeyword() {

      // given
      Limjang limjang = createLimjang(member, "어쩌구", "어쩌구", "어쩌구");
      limjangRepository.save(limjang);

      // when
      String keyword = "인창";
      List<Limjang> findLimjangs = limjangRepository.searchLimjangsWhereDeletedIsFalse(member, keyword);

      // then
      assertThat(findLimjangs)
          .hasSize(0);
    }
  }


  private Limjang createLimjang(Member member, String address, String addressDetail, String nickname) {
    return Limjang.builder()
        .memberId(member)
        .purpose(LimjangPurpose.find(0))
        .propertyType(LimjangPropertyType.find(0))
        .priceType(LimjangPriceType.find(3))
        .address(address)
        .addressDetail(addressDetail)
        .nickname(nickname)
        .build();
  }
}
