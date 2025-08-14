package umc.th.juinjang.domain.pencil.purchased.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.pencil.purchased.model.PurchasedPencil;

public interface PurchasedPencilRepository extends JpaRepository<PurchasedPencil, Long> {

	@Query("SELECT p FROM PurchasedPencil p WHERE p.member = :member AND p.deliveryStatus = 0 ORDER BY p.purchasedAt DESC")
	List<PurchasedPencil> findAllByMemberWhereDeliverySuccessOrderByCreatedAtDesc(@Param("member") Member member);

	@Query("select p from PurchasedPencil p where p.member = :member and p.remainQuantity > :remainQuantity AND p.deliveryStatus = 0 order by p.purchasedAt asc")
	List<PurchasedPencil> findByMemberAndDeliverySuccessAndRemainQuantityGreaterThanOrderByCreatedAtAsc(
		@Param("member") Member buyer,
		@Param("remainQuantity") Long remainQuantity);

	Optional<PurchasedPencil> findByTransactionIdAndMember(String transactionId, Member member);

	Optional<PurchasedPencil> findByTransactionId(String transactionId);

	@Query("SELECT SUM(p.price) FROM PurchasedPencil p " +
		"WHERE p.member = :member " +
		"AND p.deliveryStatus = 0 " +
		"AND p.transactionStatus = 'SUCCESS'")
	Optional<Long> getSumPriceWhereMemberAndSuccess(Member member);

	@Query("SELECT SUM(p.price) FROM PurchasedPencil p " +
		"WHERE p.member = :member " +
		"AND p.deliveryStatus = 0 " +
		"AND p.transactionStatus = 'REFUNDED'")
	Optional<Long> getSumPriceWhereMemberAndRefund(Member member);
}
