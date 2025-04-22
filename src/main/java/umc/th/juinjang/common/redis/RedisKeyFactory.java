package umc.th.juinjang.common.redis;

public class RedisKeyFactory {

	public static final String VIEW_COUNT = "viewcount:sharedNoteId:";
	private static final String VIEW_HISTORY = "viewed:sharedNoteId:";

	public static String viewCountKey(long sharedNoteId) {
		return VIEW_COUNT + sharedNoteId;
	}

	public static String viewHistoryKey(long sharedNoteId, long memberId) {
		return VIEW_HISTORY + sharedNoteId + ":member:" + memberId;
	}
}
