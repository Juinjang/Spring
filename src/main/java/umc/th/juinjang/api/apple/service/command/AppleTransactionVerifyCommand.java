package umc.th.juinjang.api.apple.service.command;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import umc.th.juinjang.api.pencil.controller.request.AppleIAPPurchaseRequest;

@Getter
public class AppleTransactionVerifyCommand {

	private final String transactionId;
	private final String productId;
	private final UUID appAccountToken;

	@Builder
	private AppleTransactionVerifyCommand(String transactionId, String productId, UUID appAccountToken) {
		this.transactionId = transactionId;
		this.productId = productId;
		this.appAccountToken = appAccountToken;
	}

	public static AppleTransactionVerifyCommand fromRequest(AppleIAPPurchaseRequest request) {
		return AppleTransactionVerifyCommand.builder()
			.transactionId(request.getTransactionId())
			.productId(request.getProductId())
			.appAccountToken(request.getAppAccountToken())
			.build();
	}

}
