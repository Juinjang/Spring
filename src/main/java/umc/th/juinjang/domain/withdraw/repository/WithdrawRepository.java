package umc.th.juinjang.domain.withdraw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.th.juinjang.domain.withdraw.model.Withdraw;
import umc.th.juinjang.domain.withdraw.model.WithdrawReason;

import java.util.Optional;

public interface WithdrawRepository extends JpaRepository<Withdraw, Long> {
    Optional<Withdraw> findByWithdrawReason(WithdrawReason withdrawReason);
}
