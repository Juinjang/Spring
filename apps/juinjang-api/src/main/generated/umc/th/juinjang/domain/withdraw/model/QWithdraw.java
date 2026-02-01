package umc.th.juinjang.domain.withdraw.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWithdraw is a Querydsl query type for Withdraw
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWithdraw extends EntityPathBase<Withdraw> {

    private static final long serialVersionUID = 1769459424L;

    public static final QWithdraw withdraw = new QWithdraw("withdraw");

    public final umc.th.juinjang.domain.common.QBaseEntity _super = new umc.th.juinjang.domain.common.QBaseEntity(this);

    public final NumberPath<Long> count = createNumber("count", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> withdrawId = createNumber("withdrawId", Long.class);

    public final EnumPath<WithdrawReason> withdrawReason = createEnum("withdrawReason", WithdrawReason.class);

    public QWithdraw(String variable) {
        super(Withdraw.class, forVariable(variable));
    }

    public QWithdraw(Path<? extends Withdraw> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWithdraw(PathMetadata metadata) {
        super(Withdraw.class, metadata);
    }

}

