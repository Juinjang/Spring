package umc.th.juinjang.domain.note.liked.model.repository;

import static umc.th.juinjang.domain.limjang.model.QAddress.*;
import static umc.th.juinjang.domain.limjang.model.QLimjang.*;
import static umc.th.juinjang.domain.limjang.model.QLimjangPrice.*;
import static umc.th.juinjang.domain.member.model.QMember.*;
import static umc.th.juinjang.domain.note.liked.model.QLikedNote.*;
import static umc.th.juinjang.domain.note.shared.model.QSharedNote.*;
import static umc.th.juinjang.domain.report.model.QReport.*;

import java.util.List;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import umc.th.juinjang.api.note.shared.controller.NoteType;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.member.model.QMember;
import umc.th.juinjang.domain.note.liked.model.LikedNote;
import umc.th.juinjang.domain.note.liked.model.QLikedNote;
import umc.th.juinjang.domain.note.shared.model.SharedNote;

public class LikedNoteQueryDSLRepositoryImpl implements LikedNoteQueryDSLRepository {
	private final JPAQueryFactory queryFactory;

	public LikedNoteQueryDSLRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
	}

	@Override
	public List<LikedNote> findAllByMemberAndDynamic(Member user, LimjangPropertyType propertyType,
		LimjangPriceType priceType, String keyword) {
		return queryFactory.selectFrom(likedNote)
			.join(likedNote.sharedNote, sharedNote).fetchJoin()
			.join(likedNote.member, member).fetchJoin()
			.join(sharedNote.limjang, limjang).fetchJoin()
			.join(limjang.limjangPrice, limjangPrice).fetchJoin()
			.join(limjang.addressEntity, address).fetchJoin()
			.leftJoin(limjang.report, report).fetchJoin()
			.where(
				likedNote.member.eq(user),
				getWhereByPropertyType(propertyType),
				getWhereByPriceType(priceType),
				keywordCondition(keyword))
			.orderBy(likedNote.likedNoteId.desc())
			.fetch();

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

}
