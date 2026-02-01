package umc.th.juinjang.domain.report.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReport is a Querydsl query type for Report
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReport extends EntityPathBase<Report> {

    private static final long serialVersionUID = 1337186272L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReport report = new QReport("report");

    public final umc.th.juinjang.domain.common.QBaseEntity _super = new umc.th.juinjang.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath indoorKeyword = createString("indoorKeyword");

    public final NumberPath<Float> indoorRate = createNumber("indoorRate", Float.class);

    public final umc.th.juinjang.domain.limjang.model.QLimjang limjangId;

    public final StringPath locationConditionsKeyword = createString("locationConditionsKeyword");

    public final NumberPath<Float> locationConditionsRate = createNumber("locationConditionsRate", Float.class);

    public final StringPath publicSpaceKeyword = createString("publicSpaceKeyword");

    public final NumberPath<Float> publicSpaceRate = createNumber("publicSpaceRate", Float.class);

    public final NumberPath<Long> reportId = createNumber("reportId", Long.class);

    public final NumberPath<Float> totalRate = createNumber("totalRate", Float.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QReport(String variable) {
        this(Report.class, forVariable(variable), INITS);
    }

    public QReport(Path<? extends Report> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReport(PathMetadata metadata, PathInits inits) {
        this(Report.class, metadata, inits);
    }

    public QReport(Class<? extends Report> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.limjangId = inits.isInitialized("limjangId") ? new umc.th.juinjang.domain.limjang.model.QLimjang(forProperty("limjangId"), inits.get("limjangId")) : null;
    }

}

