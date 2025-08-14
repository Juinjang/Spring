package umc.th.juinjang.domain.limjang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.model.LimjangPrice;

public interface LimjangPriceRepository extends JpaRepository<LimjangPrice, Long> {

    @Transactional
    @Modifying
    void deleteAllByLimjang(Limjang limjang);

}
