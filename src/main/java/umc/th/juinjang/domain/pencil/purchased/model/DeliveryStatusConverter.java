package umc.th.juinjang.domain.pencil.purchased.model;

import jakarta.persistence.AttributeConverter;

public class DeliveryStatusConverter implements AttributeConverter<DeliveryStatus, Integer> {

	@Override
	public Integer convertToDatabaseColumn(DeliveryStatus deliveryStatus) {
		if (deliveryStatus == null) {
			return null;
		}
		return deliveryStatus.getAppleCode();
	}

	@Override
	public DeliveryStatus convertToEntityAttribute(Integer dbData) {
		if (dbData == null) {
			return null;
		}

		for (DeliveryStatus status : DeliveryStatus.values()) {
			if (status.getAppleCode() == dbData) {
				return status;
			}
		}

		throw new IllegalArgumentException("Unknown database value: " + dbData);
	}

}
