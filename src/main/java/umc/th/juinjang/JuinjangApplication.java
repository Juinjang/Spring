package umc.th.juinjang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@ImportAutoConfiguration({FeignAutoConfiguration.class})
@EnableScheduling
public class JuinjangApplication {

	public static void main(String[] args) {
		SpringApplication.run(JuinjangApplication.class, args);
	}

}
