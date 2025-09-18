package umc.th.juinjang.api.apple.service;

import com.apple.itunes.storekit.client.APIException;
import com.apple.itunes.storekit.model.ConsumptionRequest;
import com.apple.itunes.storekit.model.Data;
import com.apple.itunes.storekit.model.JWSTransactionDecodedPayload;
import com.apple.itunes.storekit.model.ResponseBodyV2;
import com.apple.itunes.storekit.model.ResponseBodyV2DecodedPayload;
import com.apple.itunes.storekit.verification.VerificationException;
import java.io.IOException;
import umc.th.juinjang.api.apple.service.command.AppleTransactionVerifyCommand;
import umc.th.juinjang.api.pencil.service.response.VerificationResult;

public interface AppleService {

    JWSTransactionDecodedPayload getTransactionInfo(String transactionId) throws
            APIException, IOException, VerificationException;

    VerificationResult verifyAppleTransaction(AppleTransactionVerifyCommand command);

    void sendConsumptionData(String transactionId, ConsumptionRequest request);

    ResponseBodyV2DecodedPayload getNotificationPayload(ResponseBodyV2 responseBody);

    JWSTransactionDecodedPayload getSignedTransactionPayload(Data data);

}
