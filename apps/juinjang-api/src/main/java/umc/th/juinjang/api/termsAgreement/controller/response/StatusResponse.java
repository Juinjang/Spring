package umc.th.juinjang.api.termsAgreement.controller.response;

public record StatusResponse(
	boolean status
) {
	public static StatusResponse of(boolean isAgreed) {
		return new StatusResponse(isAgreed);
	}
}
