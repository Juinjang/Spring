package umc.th.juinjang.monitoring;

import static umc.th.juinjang.common.LoggerProvider.getLogger;
import static umc.th.juinjang.common.LoggerProvider.registerRequestId;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
public class ApiLoggerFilter extends OncePerRequestFilter {
	private static final Logger logger = getLogger(ApiLoggerFilter.class);
	private final ApiLoggerFactory apiLoggerFactory;
	private final List<String> EXCLUDED_URLS;

	public ApiLoggerFilter(List<String> EXCLUDED_URLS) {
		this.EXCLUDED_URLS = EXCLUDED_URLS;
		this.apiLoggerFactory = new ApiLoggerFactory();
	}

	@Override
	protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
		FilterChain chain) {
		ContentCachingRequestWrapper request = new ContentCachingRequestWrapper(servletRequest);
		ContentCachingResponseWrapper response = new ContentCachingResponseWrapper(servletResponse);
		registerRequestId(UUID.randomUUID().toString());

		try {
			if (shouldNotFilter(request)) {
				chain.doFilter(request, response);
				return;
			}
			chain.doFilter(request, response);
			apiLoggerFactory.createRequestLogger(request);
			apiLoggerFactory.createResponseLogger(response);
			response.copyBodyToResponse();
		} catch (Exception e) {
			logger.error("APILogger 필터 오류, message : {} ", e.getMessage());
		} finally {
			MDC.clear();
		}
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		AntPathMatcher pathMatcher = new AntPathMatcher();
		return EXCLUDED_URLS.stream().anyMatch(pattern -> pathMatcher.match(pattern, request.getRequestURI()));
	}
}
