package umc.th.juinjang.domain.pencil.purchased.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.pencil.purchased.model.PurchasedPencil;

public interface PurchasedPencilRepository extends JpaRepository<PurchasedPencil, Long> {

	@Query("SELECT p FROM PurchasedPencil p WHERE p.member = :member AND p.deliveryStatus = 0 ORDER BY p.createdAt DESC")
	List<PurchasedPencil> findAllByMemberWhereDeliverySuccessOrderByCreatedAtDesc(@Param("member") Member member);

	@Query("select p from PurchasedPencil p where p.member = :member and p.remainQuantity > :remainQuantity order by p.createdAt asc")
	List<PurchasedPencil> findByMemberAndRemainQuantityGreaterThanOrderByCreatedAtAsc(@Param("member") Member buyer,
		@Param("remainQuantity") Long remainQuantity);
}
