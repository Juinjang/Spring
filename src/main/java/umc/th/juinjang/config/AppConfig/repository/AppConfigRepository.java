package umc.th.juinjang.config.AppConfig.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import umc.th.juinjang.config.AppConfig.model.AppConfig;

public interface AppConfigRepository extends JpaRepository<AppConfig, String> {
}
