package umc.th.juinjang.domain.limjang.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import umc.th.juinjang.domain.limjang.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
