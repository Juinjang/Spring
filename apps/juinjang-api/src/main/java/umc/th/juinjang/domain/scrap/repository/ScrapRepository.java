package umc.th.juinjang.domain.scrap.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.scrap.model.Scrap;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

  Optional<Scrap> findByLimjangId(Limjang limjang);

  @Transactional
  @Modifying
  @Query(value = "DELETE FROM scrap s WHERE s.limjang_id = :limjangId", nativeQuery = true)
  void deleteByLimjangId(@Param("limjangId") Long limjangId);

  List<Scrap> findAllByLimjangIdIn(List<Limjang> limjangs);
}