package umc.th.juinjang.api.apple.service;

import com.apple.itunes.storekit.client.APIException;
import com.apple.itunes.storekit.model.ConsumptionRequest;
import com.apple.itunes.storekit.model.Data;
import com.apple.itunes.storekit.model.JWSTransactionDecodedPayload;
import com.apple.itunes.storekit.model.ResponseBodyV2;
import com.apple.itunes.storekit.model.ResponseBodyV2DecodedPayload;
import com.apple.itunes.storekit.verification.VerificationException;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import umc.th.juinjang.api.apple.service.command.AppleTransactionVerifyCommand;
import umc.th.juinjang.api.pencil.service.response.VerificationResult;

@Slf4j
@Service
@ConditionalOnProperty(
        name = "apple.iap.enabled",
        havingValue = "false",
        matchIfMissing = true
)
public class AppleServiceStub implements AppleService {

    @Override
    public JWSTransactionDecodedPayload getTransactionInfo(String transactionId) throws
            APIException, IOException, VerificationException {
        log.warn("Apple IAP가 비활성화되어 있습니다. TransactionId: {}", transactionId);
        throw new UnsupportedOperationException("Apple IAP is disabled");
    }

    @Override
    public VerificationResult verifyAppleTransaction(AppleTransactionVerifyCommand command) {
        log.warn("Apple IAP가 비활성화되어 있습니다. TransactionId: {}", command.getTransactionId());
        return VerificationResult.ofServerError();
    }

    @Override
    public void sendConsumptionData(String transactionId, ConsumptionRequest request) {
        log.warn("Apple IAP가 비활성화되어 있습니다. sendConsumptionData 호출 무시");
    }

    @Override
    public ResponseBodyV2DecodedPayload getNotificationPayload(ResponseBodyV2 responseBody) {
        log.warn("Apple IAP가 비활성화되어 있습니다. getNotificationPayload 호출");
        throw new UnsupportedOperationException("Apple IAP is disabled");
    }

    @Override
    public JWSTransactionDecodedPayload getSignedTransactionPayload(Data data) {
        log.warn("Apple IAP가 비활성화되어 있습니다. getSignedTransactionPayload 호출");
        throw new UnsupportedOperationException("Apple IAP is disabled");
    }
}
