package umc.th.juinjang.domain.pencil.purchased.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import umc.th.juinjang.domain.pencil.purchased.model.PurchasedPencil;

public interface PurchasedPencilRepository extends JpaRepository<PurchasedPencil, Long> {
}
