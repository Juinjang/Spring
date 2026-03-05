package umc.th.juinjang.domain.limjang.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLimjang is a Querydsl query type for Limjang
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLimjang extends EntityPathBase<Limjang> {

    private static final long serialVersionUID = 1473877658L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLimjang limjang = new QLimjang("limjang");

    public final umc.th.juinjang.domain.common.QBaseEntity _super = new umc.th.juinjang.domain.common.QBaseEntity(this);

    public final StringPath address = createString("address");

    public final StringPath addressDetail = createString("addressDetail");

    public final QAddress addressEntity;

    public final ListPath<umc.th.juinjang.domain.checklist.model.ChecklistAnswer, umc.th.juinjang.domain.checklist.model.QChecklistAnswer> answerList = this.<umc.th.juinjang.domain.checklist.model.ChecklistAnswer, umc.th.juinjang.domain.checklist.model.QChecklistAnswer>createList("answerList", umc.th.juinjang.domain.checklist.model.ChecklistAnswer.class, umc.th.juinjang.domain.checklist.model.QChecklistAnswer.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final BooleanPath deleted = createBoolean("deleted");

    public final StringPath floor = createString("floor");

    public final ListPath<umc.th.juinjang.domain.image.model.Image, umc.th.juinjang.domain.image.model.QImage> imageList = this.<umc.th.juinjang.domain.image.model.Image, umc.th.juinjang.domain.image.model.QImage>createList("imageList", umc.th.juinjang.domain.image.model.Image.class, umc.th.juinjang.domain.image.model.QImage.class, PathInits.DIRECT2);

    public final BooleanPath isSharable = createBoolean("isSharable");

    public final NumberPath<Long> limjangId = createNumber("limjangId", Long.class);

    public final QLimjangPrice limjangPrice;

    public final umc.th.juinjang.domain.member.model.QMember memberId;

    public final StringPath memo = createString("memo");

    public final StringPath nickname = createString("nickname");

    public final EnumPath<LimjangPriceType> priceType = createEnum("priceType", LimjangPriceType.class);

    public final EnumPath<LimjangPropertyType> propertyType = createEnum("propertyType", LimjangPropertyType.class);

    public final EnumPath<LimjangPurpose> purpose = createEnum("purpose", LimjangPurpose.class);

    public final NumberPath<Integer> pyong = createNumber("pyong", Integer.class);

    public final NumberPath<Integer> recordCount = createNumber("recordCount", Integer.class);

    public final ListPath<umc.th.juinjang.domain.record.model.Record, umc.th.juinjang.domain.record.model.QRecord> recordList = this.<umc.th.juinjang.domain.record.model.Record, umc.th.juinjang.domain.record.model.QRecord>createList("recordList", umc.th.juinjang.domain.record.model.Record.class, umc.th.juinjang.domain.record.model.QRecord.class, PathInits.DIRECT2);

    public final umc.th.juinjang.domain.report.model.QReport report;

    public final NumberPath<Integer> rewardPencil = createNumber("rewardPencil", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QLimjang(String variable) {
        this(Limjang.class, forVariable(variable), INITS);
    }

    public QLimjang(Path<? extends Limjang> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLimjang(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLimjang(PathMetadata metadata, PathInits inits) {
        this(Limjang.class, metadata, inits);
    }

    public QLimjang(Class<? extends Limjang> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.addressEntity = inits.isInitialized("addressEntity") ? new QAddress(forProperty("addressEntity")) : null;
        this.limjangPrice = inits.isInitialized("limjangPrice") ? new QLimjangPrice(forProperty("limjangPrice"), inits.get("limjangPrice")) : null;
        this.memberId = inits.isInitialized("memberId") ? new umc.th.juinjang.domain.member.model.QMember(forProperty("memberId")) : null;
        this.report = inits.isInitialized("report") ? new umc.th.juinjang.domain.report.model.QReport(forProperty("report"), inits.get("report")) : null;
    }

}

