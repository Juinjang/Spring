package umc.th.juinjang.domain.termsAgreement.repository;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTermsAgreement is a Querydsl query type for TermsAgreement
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTermsAgreement extends EntityPathBase<TermsAgreement> {

    private static final long serialVersionUID = 802654735L;

    public static final QTermsAgreement termsAgreement = new QTermsAgreement("termsAgreement");

    public final DateTimePath<java.time.LocalDateTime> agreedAt = createDateTime("agreedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final EnumPath<TermsType> termsType = createEnum("termsType", TermsType.class);

    public QTermsAgreement(String variable) {
        super(TermsAgreement.class, forVariable(variable));
    }

    public QTermsAgreement(Path<? extends TermsAgreement> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTermsAgreement(PathMetadata metadata) {
        super(TermsAgreement.class, metadata);
    }

}

