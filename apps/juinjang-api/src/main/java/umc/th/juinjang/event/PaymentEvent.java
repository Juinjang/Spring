package umc.th.juinjang.event;

import umc.th.juinjang.domain.pencil.purchased.model.TransactionStatus;

public record PaymentEvent(
	Long memberId,
	String nickname,
	Long price,
	Long pencilQuantity,
	TransactionStatus transactionStatus
) {
	public static PaymentEvent of(Long memberId, String nickname, Long price, Long pencilQuantity, TransactionStatus transactionStatus) {
		return new PaymentEvent(memberId, nickname, price, pencilQuantity,transactionStatus);
	}
}
