package umc.th.juinjang.monitoring;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

import org.springframework.web.util.ContentCachingResponseWrapper;

public class ApiLogResponseGenerator extends ApiLogGenerator {
	private final ContentCachingResponseWrapper response;

	public ApiLogResponseGenerator(ContentCachingResponseWrapper response) {
		this.response = response;
	}

	@Override
	public String generateLog() {
		StringBuilder logBuilder = new StringBuilder();

		logBuilder.append("Response : ").append(" ");
		// logBuilder.append(getBaseLogInfo());
		logBuilder.append("[status] ").append(response.getStatus()).append(" ");
		logBuilder.append("[responseBody] ").append(getBody(response.getContentAsByteArray())).append(" ");

		return logBuilder.toString();
	}

	protected String getBody(byte[] info) {
		String responseBody = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(new String(info, StandardCharsets.UTF_8));
			responseBody = replaceToken(rootNode.path("message").asText("N/A"));
		} catch (Exception e) {
			logger.error("responseBody 추출 오류");
		}
		return responseBody;
	}

	private String replaceToken(String responseBody) {
		return responseBody.replaceAll("\"accessToken\":\"[^\"]*\"", "\"accessToken\":\"[TOKEN]\"")
			.replaceAll("\"refreshToken\":\"[^\"]*\"", "\"refreshToken\":\"[TOKEN]\"");
	}
}
