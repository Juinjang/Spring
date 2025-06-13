package umc.th.juinjang.api.pencil.service.response;

public record AcquiredPencilReadResponse(
	boolean isMarked,
	boolean isTotalRead
) {
	public static AcquiredPencilReadResponse of(boolean isMarked, boolean isTotalRead) {
		return new AcquiredPencilReadResponse(isMarked, isTotalRead);
	}
}
