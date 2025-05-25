package umc.th.juinjang.api.apple.service.command;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import umc.th.juinjang.api.pencil.controller.request.AppleIAPPurchaseRequest;

@Getter
public class AppleTransactionVerifyCommand {

	private String transactionId;
	private String productId;
	private UUID appAccountToken;
	private String bundleId;

	@Builder
	private AppleTransactionVerifyCommand(String transactionId, String productId, UUID appAccountToken, String bundleId) {
		this.transactionId = transactionId;
		this.productId = productId;
		this.appAccountToken = appAccountToken;
		this.bundleId = bundleId;
	}

	public static AppleTransactionVerifyCommand fromRequest(AppleIAPPurchaseRequest request){
		return AppleTransactionVerifyCommand.builder().transactionId(request.getTransactionId())
			.productId(request.getProductId()).appAccountToken(request.getAppAccountToken()).bundleId(request.getProductId()).build();
	}

}
