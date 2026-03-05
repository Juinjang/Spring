package umc.th.juinjang.monitoring;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import umc.th.juinjang.monitoring.ApiLoggerFilter;

@Configuration
@Slf4j
public class ApiFilterConfig {

	@Value("${logging.api.excluded-paths}")
	private List<String> excludedUrls;

	@Bean
	public FilterRegistrationBean<ApiLoggerFilter> loggingFilter() {
		FilterRegistrationBean<ApiLoggerFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new ApiLoggerFilter(excludedUrls));
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE); // 순서 설정
		registrationBean.setUrlPatterns(List.of("/*"));
		return registrationBean;
	}
}
