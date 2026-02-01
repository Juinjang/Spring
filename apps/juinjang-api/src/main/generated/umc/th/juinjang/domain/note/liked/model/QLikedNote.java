package umc.th.juinjang.domain.note.liked.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLikedNote is a Querydsl query type for LikedNote
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLikedNote extends EntityPathBase<LikedNote> {

    private static final long serialVersionUID = 1432819984L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLikedNote likedNote = new QLikedNote("likedNote");

    public final NumberPath<Long> likedNoteId = createNumber("likedNoteId", Long.class);

    public final umc.th.juinjang.domain.member.model.QMember member;

    public final umc.th.juinjang.domain.note.shared.model.QSharedNote sharedNote;

    public QLikedNote(String variable) {
        this(LikedNote.class, forVariable(variable), INITS);
    }

    public QLikedNote(Path<? extends LikedNote> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLikedNote(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLikedNote(PathMetadata metadata, PathInits inits) {
        this(LikedNote.class, metadata, inits);
    }

    public QLikedNote(Class<? extends LikedNote> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new umc.th.juinjang.domain.member.model.QMember(forProperty("member")) : null;
        this.sharedNote = inits.isInitialized("sharedNote") ? new umc.th.juinjang.domain.note.shared.model.QSharedNote(forProperty("sharedNote"), inits.get("sharedNote")) : null;
    }

}

