package umc.th.juinjang.repository.checklist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import umc.th.juinjang.model.entity.ChecklistAnswer;
import umc.th.juinjang.model.entity.Limjang;

import java.util.List;

public interface ChecklistAnswerRepository extends JpaRepository<ChecklistAnswer, Long> {
    List<ChecklistAnswer> findChecklistAnswerByLimjangId(Limjang limjang);
    void deleteAllByLimjangId(Limjang limjang);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM checklist_answer c WHERE c.limjng_id = :limjangId", nativeQuery = true)
    void deleteByLimjangId(@Param("limjangId") Long limjangId);

}
