package umc.th.juinjang.api.appVersion.controller.response;

public record AppVersionResponse(
	String version
) {
	public static AppVersionResponse of(String version) {
		return new AppVersionResponse(version);
	}
}
