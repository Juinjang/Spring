package umc.th.juinjang.repository.limjang;

import static com.querydsl.core.types.Order.DESC;
import static umc.th.juinjang.model.entity.QImage.image;
import static umc.th.juinjang.model.entity.QLimjang.limjang;
import static umc.th.juinjang.model.entity.QLimjangPrice.limjangPrice;
import static umc.th.juinjang.model.entity.QReport.report;
import static umc.th.juinjang.model.entity.QScrap.scrap;
import static com.querydsl.core.group.GroupBy.list;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Pageable;
import umc.th.juinjang.model.dto.limjang.enums.LimjangSortOptions;
import umc.th.juinjang.model.entity.Limjang;
import umc.th.juinjang.model.entity.Member;
import umc.th.juinjang.model.entity.QReport;

public class LimjangQueryDslRepositoryImpl implements LimjangQueryDslRepository {
  private final JPAQueryFactory queryFactory;

  public LimjangQueryDslRepositoryImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
  }

  @Override
  public List<Limjang> searchLimjangsWhereDeletedIsFalse(Member member, String keyword) {
    String rKeyword = removeKeywordBlank(keyword);
    return queryFactory
        .selectFrom(limjang)
        .leftJoin(limjang.report, report).fetchJoin()
        .join(limjang.limjangPrice, limjangPrice).fetchJoin()
        .leftJoin(limjang.imageList, image).fetchJoin()
        .where(limjang.deleted.isFalse())
        .where(limjang.memberId.eq(member),
            keywordOf(
                removeBlank(limjang.nickname).containsIgnoreCase(rKeyword),
                removeBlank(limjang.address).containsIgnoreCase(rKeyword),
                removeBlank(limjang.addressDetail).containsIgnoreCase(rKeyword)
            ))
        .fetch();
  }

  private String removeKeywordBlank(String keyword) {
    return keyword.replaceAll(" ", "");
  }

  public List<Limjang> findAllByMemberAndDeletedIsFalseOrderByParam(Member member, LimjangSortOptions sort) {
    return queryFactory
        .selectFrom(limjang)
        .join(limjang.limjangPrice, limjangPrice).fetchJoin()
        .leftJoin(limjang.report, report).fetchJoin()
        .leftJoin(limjang.imageList, image).fetchJoin()
        .where(limjang.memberId.eq(member))
        .where(limjang.deleted.isFalse())
        .orderBy(getOrderByLimjangSortOptions(sort))
        .fetch();
  }

  private OrderSpecifier[] getOrderByLimjangSortOptions(LimjangSortOptions sort) {
    List<OrderSpecifier> orders = new ArrayList<>();
    switch (sort) {
      case UPDATED -> orders.add(new OrderSpecifier(DESC, limjang.updatedAt));
      case STAR -> {
        orders.add(new OrderSpecifier<>(DESC, report.totalRate.coalesce(0f), OrderSpecifier.NullHandling.NullsLast));
        orders.add(new OrderSpecifier<>(DESC, limjang.createdAt));
      }
      case CREATED -> orders.add(new OrderSpecifier<>(DESC, limjang.createdAt));
    }
    return orders.toArray(new OrderSpecifier[orders.size()]);
  }

  private BooleanExpression keywordOf(BooleanExpression... conditions) {
    BooleanExpression result = null;
    for (BooleanExpression condition : conditions) {
      result = result == null ? condition : result.or(condition);
    }
    return result;
  }

  private StringExpression removeBlank(StringExpression origin) {
    return Expressions.stringTemplate("function('replace', {0}, ' ', '')", origin);
  }

  @Override
  public List<Limjang> findMainScreenContentsLimjang(Member member) {
    return queryFactory
        .selectFrom(limjang)
        .leftJoin(limjang.report, report).fetchJoin()
        .join(limjang.limjangPrice, limjangPrice).fetchJoin()
        .where(limjang.memberId.eq(member))
        .orderBy(limjang.updatedAt.desc())
        .limit(5)
        .fetch();
  }
}
