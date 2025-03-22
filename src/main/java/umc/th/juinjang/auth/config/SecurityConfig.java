package umc.th.juinjang.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import umc.th.juinjang.auth.jwt.JwtAuthenticationFilter;
import umc.th.juinjang.auth.jwt.JwtExceptionFilter;
import umc.th.juinjang.auth.jwt.JwtService;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;

    private final JwtService jwtService;

    private final JwtExceptionFilter jwtExceptionFilter;

    private final Environment environment;
    @Bean
    @Order(0)
    public WebSecurityCustomizer webSecurityCustomizer(){
        String[] activeProfiles = environment.getActiveProfiles();
        boolean isProd = Arrays.asList(activeProfiles).contains("prod");

        //prod아닐때
        if (!isProd) {
            return web -> web.ignoring()
                    .requestMatchers("/swagger-ui/**", "/swagger/**", "/swagger-resources/**", "/swagger-ui.html", "/test",
                            "/configuration/ui",  "/v3/api-docs/**", "/h2-console/**", "/api/auth/regenerate-token",
                            "/api/auth/kakao/**", "/api/auth/apple/**", "/actuator/prometheus",
                            "/api/auth/v2/apple/**", "/api/auth/v2/kakao/**");
        }
        else {
          return web -> web.ignoring()
                    .requestMatchers("/h2-console/**", "/api/auth/regenerate-token",
                            "/api/auth/kakao/**", "/api/auth/apple/**", "/actuator/prometheus",
                            "/api/auth/v2/apple/**", "/api/auth/v2/kakao/**");
        }

    }

    //선언 방식이 3.x에서 바뀜
    @Bean AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception
    { return authConfiguration.getAuthenticationManager(); }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults())
                .sessionManagement((sessionManagement) ->
                                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                        세션을 사용하지 않는다고 설정함
                )
                .addFilter(new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration),jwtService))
//                 JwtAuthenticationFilter를 필터에 넣음
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers(
                                        AntPathRequestMatcher.antMatcher("/api/auth/**")
                                ).authenticated()
                                .requestMatchers(
                                    AntPathRequestMatcher.antMatcher("/h2-console/**")
                                ).permitAll()

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

