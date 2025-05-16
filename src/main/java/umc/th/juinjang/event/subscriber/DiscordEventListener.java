package umc.th.juinjang.event.subscriber;

import lombok.RequiredArgsConstructor;

import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import umc.th.juinjang.event.FlagSharedNoteEvent;
import umc.th.juinjang.event.SignUpEvent;
import umc.th.juinjang.external.openfeign.discord.DiscordAlertProvider;

@Component
@RequiredArgsConstructor
public class DiscordEventListener {

	private final DiscordAlertProvider discordAlertProvider;
	private final Environment environment;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Async
	public void handleSignUpEvent(SignUpEvent event) {
		if (isProdEnv()) {
			discordAlertProvider.sendMemberCreateAlertToDiscord(
				String.format(EventMessage.SIGN_UP_MESSAGE.getMessage(), event.memberProvider(), event.count(),
					event.name()));
		}
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Async
	public void handleFlagSharedNoteEvent(FlagSharedNoteEvent event) {
		discordAlertProvider.sendReportSharedNoteAlertToDiscord(String.format(
			EventMessage.FLAG_SHARED_NOTE_MESSAGE.getMessage(),
			event.flaggedByMemberId(),
			event.flagSharedNoteType().getDescription(),
			event.targetMemberId(),
			event.targetSharedNoteId()
		));
	}

	private boolean isProdEnv() {
		return environment.acceptsProfiles(Profiles.of("prod"));
	}
}
