package umc.th.juinjang.domain.pencilaccount.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import umc.th.juinjang.domain.pencilaccount.model.PencilAccount;

@Repository
public interface PencilAccountRepository extends JpaRepository<PencilAccount, Long> {
}
