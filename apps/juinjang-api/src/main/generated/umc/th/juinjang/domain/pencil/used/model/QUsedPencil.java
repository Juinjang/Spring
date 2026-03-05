package umc.th.juinjang.domain.pencil.used.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUsedPencil is a Querydsl query type for UsedPencil
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUsedPencil extends EntityPathBase<UsedPencil> {

    private static final long serialVersionUID = 2069121042L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUsedPencil usedPencil = new QUsedPencil("usedPencil");

    public final umc.th.juinjang.domain.common.QBaseEntity _super = new umc.th.juinjang.domain.common.QBaseEntity(this);

    public final StringPath buildingName = createString("buildingName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final umc.th.juinjang.domain.member.model.QMember member;

    public final NumberPath<Long> remainQuantity = createNumber("remainQuantity", Long.class);

    public final NumberPath<Long> sharedNoteId = createNumber("sharedNoteId", Long.class);

    public final EnumPath<Usedtype> type = createEnum("type", Usedtype.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> usedPencilId = createNumber("usedPencilId", Long.class);

    public final NumberPath<Long> usedQuantity = createNumber("usedQuantity", Long.class);

    public QUsedPencil(String variable) {
        this(UsedPencil.class, forVariable(variable), INITS);
    }

    public QUsedPencil(Path<? extends UsedPencil> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUsedPencil(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUsedPencil(PathMetadata metadata, PathInits inits) {
        this(UsedPencil.class, metadata, inits);
    }

    public QUsedPencil(Class<? extends UsedPencil> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new umc.th.juinjang.domain.member.model.QMember(forProperty("member")) : null;
    }

}

