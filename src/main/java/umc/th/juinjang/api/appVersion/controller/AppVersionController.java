package umc.th.juinjang.api.appVersion.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.appVersion.controller.response.AppVersionResponse;
import umc.th.juinjang.apiPayload.ApiResponse;
import umc.th.juinjang.config.AppVersionProperties;

@RestController
@RequestMapping("/api/app/version")
@RequiredArgsConstructor
public class AppVersionController {

	private final AppVersionProperties appVersionProperties;

	@GetMapping("/ios")
	public ApiResponse<AppVersionResponse> getIOSVersion() {
		return ApiResponse.onSuccess(AppVersionResponse.of(appVersionProperties.getIos()));
	}

}
