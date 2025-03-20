package umc.th.juinjang.domain.image.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import umc.th.juinjang.domain.image.model.Image;
import umc.th.juinjang.domain.limjang.model.Limjang;

public interface ImageRepository extends JpaRepository<Image, Long> {

  List<Image> findImagesByLimjangId(Limjang limjang);

  @Transactional
  @Modifying
  @Query(value = "DELETE FROM image i WHERE i.limjang_id = :limjangId", nativeQuery = true)
  void deleteByLimjangId(@Param("limjangId") Long limjangId);

}
