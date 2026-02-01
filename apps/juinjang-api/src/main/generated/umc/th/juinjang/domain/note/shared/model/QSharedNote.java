package umc.th.juinjang.domain.note.shared.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSharedNote is a Querydsl query type for SharedNote
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSharedNote extends EntityPathBase<SharedNote> {

    private static final long serialVersionUID = -1789647012L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSharedNote sharedNote = new QSharedNote("sharedNote");

    public final umc.th.juinjang.domain.common.QBaseEntity _super = new umc.th.juinjang.domain.common.QBaseEntity(this);

    public final StringPath buildingName = createString("buildingName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.sql.Timestamp> deletedAt = createDateTime("deletedAt", java.sql.Timestamp.class);

    public final BooleanPath isImageShared = createBoolean("isImageShared");

    public final NumberPath<Long> likeCount = createNumber("likeCount", Long.class);

    public final umc.th.juinjang.domain.limjang.model.QLimjang limjang;

    public final umc.th.juinjang.domain.member.model.QMember member;

    public final NumberPath<Integer> month = createNumber("month", Integer.class);

    public final StringPath period = createString("period");

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final StringPath review = createString("review");

    public final NumberPath<Long> sharedNoteId = createNumber("sharedNoteId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QSharedNote(String variable) {
        this(SharedNote.class, forVariable(variable), INITS);
    }

    public QSharedNote(Path<? extends SharedNote> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSharedNote(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSharedNote(PathMetadata metadata, PathInits inits) {
        this(SharedNote.class, metadata, inits);
    }

    public QSharedNote(Class<? extends SharedNote> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.limjang = inits.isInitialized("limjang") ? new umc.th.juinjang.domain.limjang.model.QLimjang(forProperty("limjang"), inits.get("limjang")) : null;
        this.member = inits.isInitialized("member") ? new umc.th.juinjang.domain.member.model.QMember(forProperty("member")) : null;
    }

}

