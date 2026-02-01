package umc.th.juinjang.config.AppConfig.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
public class AppConfig {

    @Id
    private String configKey;

    private String configValue;

    private String description;

    private LocalDateTime updatedAt;
}
