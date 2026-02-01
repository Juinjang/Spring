package umc.th.juinjang.domain.note.shared.repository;

import static com.querydsl.core.types.Order.*;
import static umc.th.juinjang.domain.limjang.model.QAddress.*;
import static umc.th.juinjang.domain.limjang.model.QLimjang.*;
import static umc.th.juinjang.domain.limjang.model.QLimjangPrice.*;
import static umc.th.juinjang.domain.member.model.QMember.*;
import static umc.th.juinjang.domain.note.shared.model.QSharedNote.*;
import static umc.th.juinjang.domain.pencil.used.model.QUsedPencil.*;
import static umc.th.juinjang.domain.report.model.QReport.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import umc.th.juinjang.api.note.shared.controller.request.ExploreSortType;
import umc.th.juinjang.api.note.shared.controller.request.NoteType;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.note.shared.model.SharedNote;
import umc.th.juinjang.domain.pencil.used.model.Usedtype;

public class SharedNoteQueryDSLRepositoryImpl implements SharedNoteQueryDSLRepository {
	private final JPAQueryFactory queryFactory;

	public SharedNoteQueryDSLRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
	}

	@Override
	public Page<SharedNote> findSharedNoteInExployer(List<String> code, ExploreSortType sort,
		LimjangPropertyType propertyType, LimjangPriceType priceType, String keyword, Pageable pageable) {

		List<SharedNote> content = queryFactory.selectFrom(sharedNote)
			.join(sharedNote.limjang, limjang).fetchJoin()
			.join(sharedNote.member, member).fetchJoin()
			.join(limjang.limjangPrice, limjangPrice).fetchJoin()
			.join(limjang.addressEntity, address).fetchJoin()
			.leftJoin(limjang.report, report).fetchJoin()
			.where(
				getBcodesStartsWith(code),
				getWhereByPropertyType(propertyType),
				getWhereByPriceType(priceType),
				keywordCondition(keyword),
				sharedNote.deletedAt.isNull(),
				limjang.deleted.isFalse()
			)
			.orderBy(getOrderBySortOptions(sort))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(sharedNote.count()).from(sharedNote)
			.join(sharedNote.limjang, limjang)
			.join(sharedNote.member, member)
			.join(limjang.limjangPrice, limjangPrice)
			.join(limjang.addressEntity, address)
			.leftJoin(limjang.report, report)
			.where(
				getBcodesStartsWith(code),
				getWhereByPropertyType(propertyType),
				getWhereByPriceType(priceType),
				keywordCondition(keyword),
				sharedNote.deletedAt.isNull(),
				limjang.deleted.isFalse()
			);
		long totalCount = Optional.ofNullable(countQuery.fetchOne()).orElse(0L);
		return new PageImpl<>(content, pageable, totalCount);
	}

	@Override
	public List<SharedNote> findUserSharedNotes(Member user, NoteType noteType, LimjangPropertyType propertyType,
		LimjangPriceType priceType, String keyword, List<Long> filterIds) {

		return queryFactory.selectFrom(sharedNote)
			.join(sharedNote.limjang, limjang).fetchJoin()
			.join(sharedNote.member, member).fetchJoin()
			.join(limjang.limjangPrice, limjangPrice).fetchJoin()
			.join(limjang.addressEntity, address).fetchJoin()
			.leftJoin(limjang.report, report).fetchJoin()
			.where(
				getWhereByNoteType(user, noteType, filterIds),
				getWhereByPropertyType(propertyType),
				getWhereByPriceType(priceType),
				keywordCondition(keyword)
			)
			.orderBy(getOrderByNoteType(noteType))
			.fetch();
	}

	private OrderSpecifier<?>[] getOrderByNoteType(NoteType noteType) {
		if (noteType == NoteType.SHARED) {
			return new OrderSpecifier<?>[] {sharedNote.createdAt.desc()};
		}
		return new OrderSpecifier<?>[0];
	}

	private BooleanExpression getWhereByNoteType(Member user, NoteType noteType, List<Long> ids) {
		return switch (noteType) {
			case OWNED -> sharedNote.sharedNoteId.in(ids);
			case SHARED -> sharedNote.member.eq(user).and(sharedNote.deletedAt.isNull()).and(limjang.deleted.isFalse());
			default -> null;
		};
	}

	private BooleanExpression keywordCondition(String keyword) {
		if (keyword == null || keyword.isBlank()) {
			return null;
		}
		return keywordOf(
			removeBlank(sharedNote.buildingName).containsIgnoreCase(keyword),
			removeBlank(address.roadAddress).containsIgnoreCase(keyword)
		);
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

	private BooleanExpression getWhereByPriceType(LimjangPriceType priceType) {
		if (priceType == null) {
			return null;
		}
		return limjang.priceType.eq(priceType);
	}

	private BooleanExpression getWhereByPropertyType(LimjangPropertyType propertyType) {
		if (propertyType == null) {
			return null;
		}
		return limjang.propertyType.eq(propertyType);
	}

	public BooleanExpression getBcodesStartsWith(List<String> bcodes) {
		if (bcodes == null || bcodes.isEmpty()) {
			return null;
		}

		BooleanExpression result = null;
		for (String bcode : bcodes) {
			BooleanExpression condition = address.bcode.startsWith(bcode);
			if (result == null) {
				result = condition;
			} else {
				result = result.or(condition);
			}
		}
		return result;
	}

	private OrderSpecifier<?>[] getOrderBySortOptions(ExploreSortType sort) {
		return switch (sort) {
			case LATEST -> new OrderSpecifier<?>[] {
				new OrderSpecifier<>(DESC, limjang.updatedAt)
			};
			case POPULAR -> new OrderSpecifier<?>[] {
				new OrderSpecifier<>(
					DESC,
					JPAExpressions
						.select(usedPencil.count())
						.from(usedPencil)
						.where(
							usedPencil.sharedNoteId.eq(sharedNote.sharedNoteId),
							usedPencil.type.eq(Usedtype.OWNED)
						)
				)
			};
			default -> new OrderSpecifier<?>[0];
		};
	}
}
