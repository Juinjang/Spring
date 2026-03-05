package umc.th.juinjang.domain.pencil.purchased.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPurchasedPencil is a Querydsl query type for PurchasedPencil
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPurchasedPencil extends EntityPathBase<PurchasedPencil> {

    private static final long serialVersionUID = -910346830L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPurchasedPencil purchasedPencil = new QPurchasedPencil("purchasedPencil");

    public final ComparablePath<java.util.UUID> appAccountToken = createComparable("appAccountToken", java.util.UUID.class);

    public final EnumPath<DeliveryStatus> deliveryStatus = createEnum("deliveryStatus", DeliveryStatus.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final umc.th.juinjang.domain.member.model.QMember member;

    public final NumberPath<Integer> playTime = createNumber("playTime", Integer.class);

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final DateTimePath<java.time.LocalDateTime> purchasedAt = createDateTime("purchasedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> purchaseQuantity = createNumber("purchaseQuantity", Long.class);

    public final NumberPath<Long> remainQuantity = createNumber("remainQuantity", Long.class);

    public final NumberPath<Long> retryCount = createNumber("retryCount", Long.class);

    public final StringPath title = createString("title");

    public final StringPath transactionId = createString("transactionId");

    public final EnumPath<TransactionStatus> transactionStatus = createEnum("transactionStatus", TransactionStatus.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> usedQuantity = createNumber("usedQuantity", Long.class);

    public QPurchasedPencil(String variable) {
        this(PurchasedPencil.class, forVariable(variable), INITS);
    }

    public QPurchasedPencil(Path<? extends PurchasedPencil> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPurchasedPencil(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPurchasedPencil(PathMetadata metadata, PathInits inits) {
        this(PurchasedPencil.class, metadata, inits);
    }

    public QPurchasedPencil(Class<? extends PurchasedPencil> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new umc.th.juinjang.domain.member.model.QMember(forProperty("member")) : null;
    }

}

