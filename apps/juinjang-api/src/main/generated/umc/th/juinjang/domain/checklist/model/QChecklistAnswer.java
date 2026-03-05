package umc.th.juinjang.domain.checklist.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChecklistAnswer is a Querydsl query type for ChecklistAnswer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChecklistAnswer extends EntityPathBase<ChecklistAnswer> {

    private static final long serialVersionUID = -1311778812L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChecklistAnswer checklistAnswer = new QChecklistAnswer("checklistAnswer");

    public final umc.th.juinjang.domain.common.QBaseEntity _super = new umc.th.juinjang.domain.common.QBaseEntity(this);

    public final StringPath answer = createString("answer");

    public final NumberPath<Long> answerId = createNumber("answerId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final umc.th.juinjang.domain.limjang.model.QLimjang limjangId;

    public final QChecklistQuestionShort questionId;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QChecklistAnswer(String variable) {
        this(ChecklistAnswer.class, forVariable(variable), INITS);
    }

    public QChecklistAnswer(Path<? extends ChecklistAnswer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChecklistAnswer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChecklistAnswer(PathMetadata metadata, PathInits inits) {
        this(ChecklistAnswer.class, metadata, inits);
    }

    public QChecklistAnswer(Class<? extends ChecklistAnswer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.limjangId = inits.isInitialized("limjangId") ? new umc.th.juinjang.domain.limjang.model.QLimjang(forProperty("limjangId"), inits.get("limjangId")) : null;
        this.questionId = inits.isInitialized("questionId") ? new QChecklistQuestionShort(forProperty("questionId")) : null;
    }

}

