package umc.th.juinjang.domain.checklist.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.common.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChecklistAnswer extends BaseEntity {

  @Id
  @Column(name="answer_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long answerId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "question_id")
  private ChecklistQuestionShort questionId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "limjng_id")
  private Limjang limjangId;

  // 답변
  @Column(nullable = false)
  private String answer;


}
