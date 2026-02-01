package umc.th.juinjang.domain.pencil.acquired.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAcquiredPencil is a Querydsl query type for AcquiredPencil
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAcquiredPencil extends EntityPathBase<AcquiredPencil> {

    private static final long serialVersionUID = 2035080946L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAcquiredPencil acquiredPencil = new QAcquiredPencil("acquiredPencil");

    public final umc.th.juinjang.domain.common.QBaseEntity _super = new umc.th.juinjang.domain.common.QBaseEntity(this);

    public final NumberPath<Long> acquiredQuantity = createNumber("acquiredQuantity", Long.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isRead = createBoolean("isRead");

    public final umc.th.juinjang.domain.member.model.QMember member;

    public final NumberPath<Long> sharedNoteId = createNumber("sharedNoteId", Long.class);

    public final EnumPath<AcquiredType> type = createEnum("type", AcquiredType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAcquiredPencil(String variable) {
        this(AcquiredPencil.class, forVariable(variable), INITS);
    }

    public QAcquiredPencil(Path<? extends AcquiredPencil> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAcquiredPencil(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAcquiredPencil(PathMetadata metadata, PathInits inits) {
        this(AcquiredPencil.class, metadata, inits);
    }

    public QAcquiredPencil(Class<? extends AcquiredPencil> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new umc.th.juinjang.domain.member.model.QMember(forProperty("member")) : null;
    }

}

