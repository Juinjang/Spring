package umc.th.juinjang.domain.pencil.purchased.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryStatus {

	DELIVERY_SUCCESS(0), //The app delivered the consumable in-app purchase and it’s working properly.
	SERVER_ERROR(3), // The app didn’t deliver the consumable in-app purchase due to a server outage.
	OTHER_REASONS(5); // The app didn't deliver the consumable in-app purchase for other reasons.

	private final int appleCode;

}
