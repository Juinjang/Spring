package umc.th.juinjang.domain.image.repository;

import static umc.th.juinjang.domain.image.model.QImage.*;

import java.util.List;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import umc.th.juinjang.domain.image.model.Image;
import umc.th.juinjang.domain.limjang.model.Limjang;

public class ImageQueryDslRepositoryImpl implements ImageQueryDslRepository {
	private final JPAQueryFactory queryFactory;

	public ImageQueryDslRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
	}

	@Override
	public List<Image> findAllFirstCreatedImagePerNote(List<Limjang> limjangs) {
		return queryFactory
			.selectFrom(image)
			.where(image.imageId.in(
				subqueryFirstCreatedImagePerNote(limjangs)
			))
			.fetch();
	}

	private JPQLQuery<Long> subqueryFirstCreatedImagePerNote(List<Limjang> limjangs) {
		return JPAExpressions
			.select(image.imageId.min())
			.from(image)
			.where(image.limjangId.in(limjangs))
			.groupBy(image.limjangId);
	}
}
