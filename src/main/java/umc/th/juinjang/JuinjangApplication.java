package umc.th.juinjang;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
// @EnableAsync
@ImportAutoConfiguration({FeignAutoConfiguration.class})
@EnableScheduling
@EnableRetry
public class JuinjangApplication {

	@PostConstruct
	public void init() {
		// set timezone
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

	public static void main(String[] args) {
		SpringApplication.run(JuinjangApplication.class, args);
	}

}
