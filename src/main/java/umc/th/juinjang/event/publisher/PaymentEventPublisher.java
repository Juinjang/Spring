package umc.th.juinjang.event.publisher;

import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.pencil.purchased.model.TransactionStatus;

public interface PaymentEventPublisher {
	void publishPaymentEvent(Member buyer, Long price, Long pencilQuantity, TransactionStatus transactionStatus);
}
