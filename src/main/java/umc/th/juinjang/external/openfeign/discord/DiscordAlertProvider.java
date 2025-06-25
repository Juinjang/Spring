package umc.th.juinjang.external.openfeign.discord;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import umc.th.juinjang.external.openfeign.discord.dto.DiscordAlert;

@Component
@Slf4j
public class DiscordAlertProvider {
	private final WebClient webClient;

	@Value("${discord.member-create}")
	private String memberCreateWebhookUrl;

	@Value("${discord.report-shared-note}")
	private String reportSharedNoteWebhookUrl;

	@Value("${discord.execute-payment}")
	private String executePaymentWebhookUrl;

	public DiscordAlertProvider(WebClient.Builder builder) {
		this.webClient = builder.build();
	}

	private void sendWebClient(String url, String content) {
		webClient.post()
			.uri(url)
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(DiscordAlert.createAlert(content))
			.retrieve()
			.bodyToMono(Void.class)
			.subscribe();
	}

	public void sendMemberCreateAlertToDiscord(String content) {
		try {
			sendWebClient(memberCreateWebhookUrl, content);
		} catch (Exception e) {
			log.info(StatusMessage.DISCORD_ALERT_ERROR.getMessage() + " " + e.getMessage());
		}
	}

	public void sendReportSharedNoteAlertToDiscord(String content) {
		try {
			sendWebClient(reportSharedNoteWebhookUrl, content);
		} catch (FeignException e) {
			log.info(StatusMessage.DISCORD_ALERT_ERROR.getMessage() + " " + e.getMessage());
		}
	}

	public void sendPaymentAlertToDiscord(String content) {
		try {
			sendWebClient(executePaymentWebhookUrl, content);
		} catch (FeignException e) {
			log.info("{} {}", StatusMessage.DISCORD_ALERT_ERROR.getMessage(), e.getMessage());
		}
	}
}
