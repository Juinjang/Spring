package umc.th.juinjang.domain.flag.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFlagSharedNote is a Querydsl query type for FlagSharedNote
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFlagSharedNote extends EntityPathBase<FlagSharedNote> {

    private static final long serialVersionUID = 38297911L;

    public static final QFlagSharedNote flagSharedNote = new QFlagSharedNote("flagSharedNote");

    public final umc.th.juinjang.domain.common.QBaseEntity _super = new umc.th.juinjang.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> flagged_by_member_id = createNumber("flagged_by_member_id", Long.class);

    public final NumberPath<Long> sharedNoteFlagId = createNumber("sharedNoteFlagId", Long.class);

    public final NumberPath<Long> sharedNoteId = createNumber("sharedNoteId", Long.class);

    public final EnumPath<FlagSharedNoteStatus> status = createEnum("status", FlagSharedNoteStatus.class);

    public final EnumPath<FlagSharedNoteType> type = createEnum("type", FlagSharedNoteType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QFlagSharedNote(String variable) {
        super(FlagSharedNote.class, forVariable(variable));
    }

    public QFlagSharedNote(Path<? extends FlagSharedNote> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFlagSharedNote(PathMetadata metadata) {
        super(FlagSharedNote.class, metadata);
    }

}

