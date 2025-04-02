package umc.th.juinjang.api.address.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.limjang.model.Address;
import umc.th.juinjang.domain.limjang.repository.AddressRepository;

@Component
@RequiredArgsConstructor
public class AddressUpdater {

	private final AddressRepository addressRepository;

	public void save(Address address) {
		addressRepository.save(address);
	}
}
