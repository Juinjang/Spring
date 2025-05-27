package umc.th.juinjang.api.apple.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apple.itunes.storekit.model.ResponseBodyV2;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.apple.service.AppleService;
import umc.th.juinjang.api.dto.ApiResponse;

@RestController
@RequestMapping("/api/apple")
@RequiredArgsConstructor
public class AppleController {

	private final AppleService appleService;

	@Operation(summary = "애플 서버 알림 API")
	@PostMapping("notifications/v2")
	public ResponseEntity<Void> handleNotificationV2(@RequestBody ResponseBodyV2 requestBody){
		appleService.handleNotification(requestBody);
		return ResponseEntity.ok().build();
	}
}
