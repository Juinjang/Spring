package umc.th.juinjang.domain.checklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.th.juinjang.domain.checklist.model.ChecklistQuestionShort;

public interface ChecklistQuestionRepository extends JpaRepository<ChecklistQuestionShort, Long> {
//    List<ChecklistQuestionShort> findChecklistQuestionsByPurpose(LimjangPurpose purpose);
//
//    List<ChecklistQuestionShort> findChecklistQuestionsByPurposeAndCategory(LimjangPurpose purpose, ChecklistQuestionCategory category);
}
