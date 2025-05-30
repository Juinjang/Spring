package umc.th.juinjang.api.pencil.service.response;

import com.apple.itunes.storekit.model.JWSTransactionDecodedPayload;

import lombok.Builder;
import lombok.Getter;

@Getter
public class VerificationResult {


	public enum Status { SUCCESS, INVALID, IO_ERROR, SERVER_ERROR, VERIFICATION_ERROR }

	private final Status status;
	private final JWSTransactionDecodedPayload payload;

	@Builder
	private VerificationResult(Status status, JWSTransactionDecodedPayload payload) {
		this.status = status;
		this.payload = payload;
	}

	public static VerificationResult ofSuccess(JWSTransactionDecodedPayload payload) {
		 return VerificationResult.builder().status(Status.SUCCESS).payload(payload).build();
	}

	public static VerificationResult ofServerError() {
		return VerificationResult.builder().status(Status.SERVER_ERROR).build();
	}

	public static VerificationResult ofVerificationError() {
		return VerificationResult.builder().status(Status.VERIFICATION_ERROR).build();
	}

	public static boolean isSuccess(VerificationResult verificationResult) {
		return verificationResult.getStatus() == Status.SUCCESS;
	}
}
