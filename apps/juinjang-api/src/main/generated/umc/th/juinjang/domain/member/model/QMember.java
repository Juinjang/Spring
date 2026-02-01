package umc.th.juinjang.domain.member.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -2003808864L;

    public static final QMember member = new QMember("member1");

    public final umc.th.juinjang.domain.common.QBaseEntity _super = new umc.th.juinjang.domain.common.QBaseEntity(this);

    public final StringPath agreeVersion = createString("agreeVersion");

    public final StringPath appleSub = createString("appleSub");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final StringPath imageUrl = createString("imageUrl");

    public final StringPath introduction = createString("introduction");

    public final NumberPath<Long> kakaoTargetId = createNumber("kakaoTargetId", Long.class);

    public final ListPath<umc.th.juinjang.domain.note.liked.model.LikedNote, umc.th.juinjang.domain.note.liked.model.QLikedNote> likedNotes = this.<umc.th.juinjang.domain.note.liked.model.LikedNote, umc.th.juinjang.domain.note.liked.model.QLikedNote>createList("likedNotes", umc.th.juinjang.domain.note.liked.model.LikedNote.class, umc.th.juinjang.domain.note.liked.model.QLikedNote.class, PathInits.DIRECT2);

    public final ListPath<umc.th.juinjang.domain.limjang.model.Limjang, umc.th.juinjang.domain.limjang.model.QLimjang> limjangList = this.<umc.th.juinjang.domain.limjang.model.Limjang, umc.th.juinjang.domain.limjang.model.QLimjang>createList("limjangList", umc.th.juinjang.domain.limjang.model.Limjang.class, umc.th.juinjang.domain.limjang.model.QLimjang.class, PathInits.DIRECT2);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath nickname = createString("nickname");

    public final ListPath<umc.th.juinjang.domain.pencilaccount.model.PencilAccount, umc.th.juinjang.domain.pencilaccount.model.QPencilAccount> pencilAccounts = this.<umc.th.juinjang.domain.pencilaccount.model.PencilAccount, umc.th.juinjang.domain.pencilaccount.model.QPencilAccount>createList("pencilAccounts", umc.th.juinjang.domain.pencilaccount.model.PencilAccount.class, umc.th.juinjang.domain.pencilaccount.model.QPencilAccount.class, PathInits.DIRECT2);

    public final EnumPath<MemberProvider> provider = createEnum("provider", MemberProvider.class);

    public final ListPath<umc.th.juinjang.domain.pencil.purchased.model.PurchasedPencil, umc.th.juinjang.domain.pencil.purchased.model.QPurchasedPencil> purchasedPencils = this.<umc.th.juinjang.domain.pencil.purchased.model.PurchasedPencil, umc.th.juinjang.domain.pencil.purchased.model.QPurchasedPencil>createList("purchasedPencils", umc.th.juinjang.domain.pencil.purchased.model.PurchasedPencil.class, umc.th.juinjang.domain.pencil.purchased.model.QPurchasedPencil.class, PathInits.DIRECT2);

    public final StringPath refreshToken = createString("refreshToken");

    public final DateTimePath<java.time.LocalDateTime> refreshTokenExpiresAt = createDateTime("refreshTokenExpiresAt", java.time.LocalDateTime.class);

    public final ListPath<umc.th.juinjang.domain.note.shared.model.SharedNote, umc.th.juinjang.domain.note.shared.model.QSharedNote> sharedNotes = this.<umc.th.juinjang.domain.note.shared.model.SharedNote, umc.th.juinjang.domain.note.shared.model.QSharedNote>createList("sharedNotes", umc.th.juinjang.domain.note.shared.model.SharedNote.class, umc.th.juinjang.domain.note.shared.model.QSharedNote.class, PathInits.DIRECT2);

    public final EnumPath<MemberStatus> status = createEnum("status", MemberStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final ListPath<umc.th.juinjang.domain.pencil.used.model.UsedPencil, umc.th.juinjang.domain.pencil.used.model.QUsedPencil> usedPencils = this.<umc.th.juinjang.domain.pencil.used.model.UsedPencil, umc.th.juinjang.domain.pencil.used.model.QUsedPencil>createList("usedPencils", umc.th.juinjang.domain.pencil.used.model.UsedPencil.class, umc.th.juinjang.domain.pencil.used.model.QUsedPencil.class, PathInits.DIRECT2);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

