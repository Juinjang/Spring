package umc.th.juinjang.api.appVersion.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.th.juinjang.config.AppConfig.model.AppConfig;
import umc.th.juinjang.config.AppConfig.repository.AppConfigRepository;

@RequiredArgsConstructor
@Service
public class AppVersionService {

    private final AppConfigRepository appConfigRepository;

    public String getIosVersion() {
        final String APPLE_VERSION_CONFIG_KEY = "app.version.ios";
        return appConfigRepository.findById(APPLE_VERSION_CONFIG_KEY)
                .map(AppConfig::getConfigValue)
                .orElseThrow(() -> new IllegalStateException("iOS version config not found"));
    }

}
