package umc.th.juinjang.domain.pencil.used.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import umc.th.juinjang.domain.pencil.used.model.UsedPencil;

@Repository
public interface UsedPencilRepository extends JpaRepository<UsedPencil, Long> {
}
