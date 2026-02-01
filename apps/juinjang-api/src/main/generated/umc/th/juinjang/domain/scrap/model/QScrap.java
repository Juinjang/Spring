package umc.th.juinjang.domain.scrap.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QScrap is a Querydsl query type for Scrap
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QScrap extends EntityPathBase<Scrap> {

    private static final long serialVersionUID = -1231813892L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QScrap scrap = new QScrap("scrap");

    public final umc.th.juinjang.domain.common.QBaseEntity _super = new umc.th.juinjang.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final umc.th.juinjang.domain.limjang.model.QLimjang limjangId;

    public final NumberPath<Long> scrapId = createNumber("scrapId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QScrap(String variable) {
        this(Scrap.class, forVariable(variable), INITS);
    }

    public QScrap(Path<? extends Scrap> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QScrap(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QScrap(PathMetadata metadata, PathInits inits) {
        this(Scrap.class, metadata, inits);
    }

    public QScrap(Class<? extends Scrap> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.limjangId = inits.isInitialized("limjangId") ? new umc.th.juinjang.domain.limjang.model.QLimjang(forProperty("limjangId"), inits.get("limjangId")) : null;
    }

}

