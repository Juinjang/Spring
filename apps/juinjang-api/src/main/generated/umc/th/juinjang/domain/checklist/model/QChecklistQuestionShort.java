package umc.th.juinjang.domain.checklist.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChecklistQuestionShort is a Querydsl query type for ChecklistQuestionShort
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChecklistQuestionShort extends EntityPathBase<ChecklistQuestionShort> {

    private static final long serialVersionUID = -159808208L;

    public static final QChecklistQuestionShort checklistQuestionShort = new QChecklistQuestionShort("checklistQuestionShort");

    public final umc.th.juinjang.domain.common.QBaseEntity _super = new umc.th.juinjang.domain.common.QBaseEntity(this);

    public final ListPath<ChecklistAnswer, QChecklistAnswer> answerList = this.<ChecklistAnswer, QChecklistAnswer>createList("answerList", ChecklistAnswer.class, QChecklistAnswer.class, PathInits.DIRECT2);

    public final EnumPath<ChecklistQuestionType> answerType = createEnum("answerType", ChecklistQuestionType.class);

    public final EnumPath<ChecklistQuestionCategory> category = createEnum("category", ChecklistQuestionCategory.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> questionId = createNumber("questionId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QChecklistQuestionShort(String variable) {
        super(ChecklistQuestionShort.class, forVariable(variable));
    }

    public QChecklistQuestionShort(Path<? extends ChecklistQuestionShort> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChecklistQuestionShort(PathMetadata metadata) {
        super(ChecklistQuestionShort.class, metadata);
    }

}

