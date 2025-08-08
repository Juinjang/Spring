package umc.th.juinjang.auth.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.auth.jwt.JwtAuthenticationFilter;
import umc.th.juinjang.auth.jwt.JwtExceptionFilter;
import umc.th.juinjang.auth.jwt.JwtService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	// 공통적으로 허용되는 URL 패턴
	private static final String[] COMMON_WHITELIST_URLS = {
		"/h2-console/**",
		"/api/auth/regenerate-token",
		"/api/auth/kakao/**",
		"/api/auth/apple/**",
		"/actuator/prometheus",
		"/api/auth/v2/apple/**",
		"/api/auth/v2/kakao/**",
		"/api/members/nickname/exists",
		"/api/app/version/ios"
	};
	// 개발 환경에서만 추가로 허용되는 URL 패턴
	private static final String[] DEV_WHITELIST_URLS = {
		"/swagger-ui/**",
		"/swagger/**",
		"/swagger-resources/**",
		"/swagger-ui.html",
		"/test",
		"/configuration/ui",
		"/v3/api-docs/**"
	};
	private final AuthenticationConfiguration authenticationConfiguration;
	private final JwtService jwtService;
	private final JwtExceptionFilter jwtExceptionFilter;
	private final Environment environment;

	@Bean
	@Order(0)
	public WebSecurityCustomizer webSecurityCustomizer() {
		String[] activeProfiles = environment.getActiveProfiles();
		boolean isProd = Arrays.asList(activeProfiles).contains("prod");

		//prod아닐때
		if (!isProd) {
			return web -> web.ignoring()
				.requestMatchers(COMMON_WHITELIST_URLS)
				.requestMatchers(DEV_WHITELIST_URLS);
		} else {
			return web -> web.ignoring()
				.requestMatchers(COMMON_WHITELIST_URLS);
		}

	}

	//선언 방식이 3.x에서 바뀜
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
		return authConfiguration.getAuthenticationManager();
	}

	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.sessionManagement((sessionManagement) ->
					sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				//                        세션을 사용하지 않는다고 설정함
			)
			.addFilter(new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtService))
			//                 JwtAuthenticationFilter를 필터에 넣음
			.authorizeHttpRequests((authorizeRequests) ->
				authorizeRequests
					.requestMatchers(
						AntPathRequestMatcher.antMatcher("/api/members/nickname/exists"),
						AntPathRequestMatcher.antMatcher("/api/app/version/ios"),
						AntPathRequestMatcher.antMatcher("/h2-console/**")
					).permitAll()
					.requestMatchers(
						AntPathRequestMatcher.antMatcher("/api/auth/**")
					).authenticated()
					.anyRequest().authenticated()

			)
			.headers(
				headersConfigurer ->
					headersConfigurer
						.frameOptions(
							HeadersConfigurer.FrameOptionsConfig::sameOrigin
						)
			)
			.addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);

		return http.build();
	}

}

