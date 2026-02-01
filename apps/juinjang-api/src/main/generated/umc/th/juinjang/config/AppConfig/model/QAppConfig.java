package umc.th.juinjang.config.AppConfig.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAppConfig is a Querydsl query type for AppConfig
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAppConfig extends EntityPathBase<AppConfig> {

    private static final long serialVersionUID = 473934270L;

    public static final QAppConfig appConfig = new QAppConfig("appConfig");

    public final StringPath configKey = createString("configKey");

    public final StringPath configValue = createString("configValue");

    public final StringPath description = createString("description");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QAppConfig(String variable) {
        super(AppConfig.class, forVariable(variable));
    }

    public QAppConfig(Path<? extends AppConfig> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAppConfig(PathMetadata metadata) {
        super(AppConfig.class, metadata);
    }

}

