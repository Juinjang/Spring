package umc.th.juinjang.api.note.shared.service.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class SharedNotesTimeAgoFormatter {
	public static String getTimeAge(LocalDateTime createdAt) {
		LocalDateTime now = LocalDateTime.now();
		Duration duration = Duration.between(createdAt, now);
		long seconds = duration.getSeconds();

		if (seconds < 600) {
			return "방금 전";
		} else if (seconds < 3600) {
			long minutes = seconds / 60;
			return minutes + "분 전";
		} else if (seconds < 86400) {
			long hours = seconds / 3600;
			return hours + "시간 전";
		}

		LocalDate createdDate = createdAt.toLocalDate();
		LocalDate currentDate = now.toLocalDate();
		Period period = Period.between(createdDate, currentDate);

		if (period.getYears() >= 1) {
			return period.getYears() + "년 전";
		} else if (period.getMonths() >= 1) {
			return period.getMonths() + "개월 전";
		} else {
			return period.getDays() + "일 전";
		}
	}
}
