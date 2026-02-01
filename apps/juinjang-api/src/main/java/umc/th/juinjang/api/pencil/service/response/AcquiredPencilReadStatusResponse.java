package umc.th.juinjang.api.pencil.service.response;

public record AcquiredPencilReadStatusResponse(
	boolean isTotalRead
) {
	public static AcquiredPencilReadStatusResponse of(boolean isTotalRead) {
		return new AcquiredPencilReadStatusResponse(isTotalRead);
	}
}
