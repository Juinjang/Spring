package umc.th.juinjang.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "app.version")
@Getter
@Setter
public class AppVersionProperties {

	/**
	 * IOS 의 어플 최신 버전
	 */
	private String ios;
}
