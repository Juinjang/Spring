package umc.th.juinjang.api.limjang.service.response;

import java.util.List;

public record ChecklistConditionResponse(
	boolean isTotalSatisfied,
	List<CategoryCondition> conditions
) {
	public record CategoryCondition(
		String category,
		int answeredCount,
		int totalCount,
		int requiredCount,
		boolean isSatisfied
	) {
	}
}
