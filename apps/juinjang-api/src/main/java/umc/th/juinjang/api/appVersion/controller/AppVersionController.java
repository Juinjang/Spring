package umc.th.juinjang.api.appVersion.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.th.juinjang.api.appVersion.controller.response.AppVersionResponse;
import umc.th.juinjang.api.appVersion.service.AppVersionService;
import umc.th.juinjang.api.dto.ApiResponse;

@RestController
@RequestMapping("/api/app/version")
@RequiredArgsConstructor
public class AppVersionController {

    private final AppVersionService appVersionService;

    @GetMapping("/ios")
    public ApiResponse<AppVersionResponse> getIOSVersion() {
        return ApiResponse.onSuccess(AppVersionResponse.of(
                appVersionService.getIosVersion()
        ));
    }

}
