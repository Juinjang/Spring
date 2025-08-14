package umc.th.juinjang.external.openfeign;


import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import umc.th.juinjang.JuinjangApplication;

@Configuration
@EnableFeignClients(basePackageClasses = JuinjangApplication.class)
public class FeignClientConfig {
}
