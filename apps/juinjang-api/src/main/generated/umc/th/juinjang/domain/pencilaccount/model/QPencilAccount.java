package umc.th.juinjang.domain.pencilaccount.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPencilAccount is a Querydsl query type for PencilAccount
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPencilAccount extends EntityPathBase<PencilAccount> {

    private static final long serialVersionUID = -347828550L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPencilAccount pencilAccount = new QPencilAccount("pencilAccount");

    public final umc.th.juinjang.domain.common.QBaseEntity _super = new umc.th.juinjang.domain.common.QBaseEntity(this);

    public final NumberPath<Long> acquiredBalance = createNumber("acquiredBalance", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final umc.th.juinjang.domain.member.model.QMember member;

    public final NumberPath<Long> pencilAccountId = createNumber("pencilAccountId", Long.class);

    public final NumberPath<Long> purchasedBalance = createNumber("purchasedBalance", Long.class);

    public final NumberPath<Long> totalBalance = createNumber("totalBalance", Long.class);

    public final NumberPath<Long> totalPurchaseAmount = createNumber("totalPurchaseAmount", Long.class);

    public final NumberPath<Long> totalRefundAmount = createNumber("totalRefundAmount", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPencilAccount(String variable) {
        this(PencilAccount.class, forVariable(variable), INITS);
    }

    public QPencilAccount(Path<? extends PencilAccount> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPencilAccount(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPencilAccount(PathMetadata metadata, PathInits inits) {
        this(PencilAccount.class, metadata, inits);
    }

    public QPencilAccount(Class<? extends PencilAccount> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new umc.th.juinjang.domain.member.model.QMember(forProperty("member")) : null;
    }

}

