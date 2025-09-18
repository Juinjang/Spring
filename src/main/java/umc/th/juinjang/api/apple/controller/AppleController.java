package umc.th.juinjang.api.apple.controller;

import com.apple.itunes.storekit.model.Data;
import com.apple.itunes.storekit.model.JWSTransactionDecodedPayload;
import com.apple.itunes.storekit.model.NotificationTypeV2;
import com.apple.itunes.storekit.model.ResponseBodyV2;
import com.apple.itunes.storekit.model.ResponseBodyV2DecodedPayload;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.th.juinjang.api.apple.service.AppleService;
import umc.th.juinjang.api.pencil.service.PencilCommandService;
import umc.th.juinjang.api.pencil.service.PencilQueryService;

@RestController
@RequestMapping("/api/apple")
@RequiredArgsConstructor
@Slf4j
public class AppleController {

    private final AppleService appleService;
    private final PencilQueryService pencilQueryService;
    private final PencilCommandService pencilCommandService;

    @Operation(summary = "애플 서버 알림 API")
    @PostMapping("notifications/v2")
    public ResponseEntity<Void> handleNotificationV2(@RequestBody ResponseBodyV2 requestBody) {
        ResponseBodyV2DecodedPayload payload = appleService.getNotificationPayload(requestBody);
        NotificationTypeV2 type = payload.getNotificationType();
        log.info("### Notification Type: {}", type);

        Data data = payload.getData();
        JWSTransactionDecodedPayload transactionPayload =
                appleService.getSignedTransactionPayload(data);
        if (type == NotificationTypeV2.CONSUMPTION_REQUEST) {
            log.info("Apple IAP Consumption Request Notification Received.");
            String transactionId = transactionPayload.getTransactionId();
            appleService.sendConsumptionData(transactionId, pencilQueryService.getConsumptionRequest(transactionId));
        } else if (type == NotificationTypeV2.REFUND) {
            log.info("Apple IAP ReFund Notification Received.");
            String transactionId = transactionPayload.getOriginalTransactionId();
            pencilCommandService.handleRefundPurchase(transactionId);
        }
        return ResponseEntity.ok().build();
    }
}
