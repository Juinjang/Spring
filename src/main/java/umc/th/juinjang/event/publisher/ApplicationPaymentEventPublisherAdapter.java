package umc.th.juinjang.event.publisher;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.pencil.purchased.model.TransactionStatus;
import umc.th.juinjang.event.PaymentEvent;

@RequiredArgsConstructor
@Component
public class ApplicationPaymentEventPublisherAdapter implements PaymentEventPublisher {

	private final ApplicationEventPublisher applicationEventPublisher;

	@Override
	public void publishPaymentEvent(Member buyer, Long price, Long pencilQuantity, TransactionStatus transactionStatus) {
		applicationEventPublisher.publishEvent(
			PaymentEvent.of(
				buyer.getMemberId(),
				buyer.getNickname(),
				price,
				pencilQuantity,
				transactionStatus
			)
		);
	}
}
