package umc.th.juinjang.domain.limjang.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLimjangPrice is a Querydsl query type for LimjangPrice
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLimjangPrice extends EntityPathBase<LimjangPrice> {

    private static final long serialVersionUID = -1441056049L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLimjangPrice limjangPrice = new QLimjangPrice("limjangPrice");

    public final umc.th.juinjang.domain.common.QBaseEntity _super = new umc.th.juinjang.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath depositPrice = createString("depositPrice");

    public final QLimjang limjang;

    public final StringPath marketPrice = createString("marketPrice");

    public final StringPath monthlyRent = createString("monthlyRent");

    public final NumberPath<Long> priceId = createNumber("priceId", Long.class);

    public final StringPath pullRent = createString("pullRent");

    public final StringPath sellingPrice = createString("sellingPrice");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QLimjangPrice(String variable) {
        this(LimjangPrice.class, forVariable(variable), INITS);
    }

    public QLimjangPrice(Path<? extends LimjangPrice> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLimjangPrice(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLimjangPrice(PathMetadata metadata, PathInits inits) {
        this(LimjangPrice.class, metadata, inits);
    }

    public QLimjangPrice(Class<? extends LimjangPrice> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.limjang = inits.isInitialized("limjang") ? new QLimjang(forProperty("limjang"), inits.get("limjang")) : null;
    }

}

