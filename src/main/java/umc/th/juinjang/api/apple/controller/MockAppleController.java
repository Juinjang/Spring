package umc.th.juinjang.api.apple.controller;

import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apple.itunes.storekit.model.ConsumptionRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.api.pencil.service.PencilCommandService;
import umc.th.juinjang.api.pencil.service.PencilQueryService;

@RestController
@RequestMapping("/api/apple/mock")
@RequiredArgsConstructor
@Slf4j
@Profile("dev")
public class MockAppleController {

	private final PencilQueryService pencilQueryService;
	private final PencilCommandService pencilCommandService;

	@PostMapping("/refund")
	public ResponseEntity<Void> mockRefund(
		@RequestBody Map<String, String> requestBody
	) {
		String transactionId = requestBody.get("transactionId");
		pencilCommandService.handleRefundPurchase(transactionId);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/consumption/request")
	public ResponseEntity<ConsumptionRequest> mockConsumptionRequest(
		@RequestBody Map<String, String> requestBody
	) {
		String transactionId = requestBody.get("transactionId");
		ConsumptionRequest result = pencilQueryService.getConsumptionRequest(transactionId);
		return ResponseEntity.ok(result);
	}

}
