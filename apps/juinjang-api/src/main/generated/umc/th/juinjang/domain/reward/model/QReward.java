package umc.th.juinjang.domain.reward.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReward is a Querydsl query type for Reward
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReward extends EntityPathBase<Reward> {

    private static final long serialVersionUID = 277161920L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReward reward = new QReward("reward");

    public final umc.th.juinjang.domain.member.model.QMember member;

    public final NumberPath<Long> milestone = createNumber("milestone", Long.class);

    public final NumberPath<Long> rewardId = createNumber("rewardId", Long.class);

    public final NumberPath<Long> rewardPencil = createNumber("rewardPencil", Long.class);

    public final NumberPath<Long> sharedNoteId = createNumber("sharedNoteId", Long.class);

    public final EnumPath<RewardType> type = createEnum("type", RewardType.class);

    public QReward(String variable) {
        this(Reward.class, forVariable(variable), INITS);
    }

    public QReward(Path<? extends Reward> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReward(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReward(PathMetadata metadata, PathInits inits) {
        this(Reward.class, metadata, inits);
    }

    public QReward(Class<? extends Reward> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new umc.th.juinjang.domain.member.model.QMember(forProperty("member")) : null;
    }

}

