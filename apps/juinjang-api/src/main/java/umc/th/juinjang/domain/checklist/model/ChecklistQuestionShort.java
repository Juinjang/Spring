package umc.th.juinjang.domain.checklist.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.th.juinjang.domain.common.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChecklistQuestionShort extends BaseEntity {

  @Id
  @Column(name="question_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long questionId;

  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private ChecklistQuestionCategory category;

  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private ChecklistQuestionType answerType;

  @OneToMany(mappedBy = "questionId", cascade = CascadeType.ALL)
  private List<ChecklistAnswer> answerList = new ArrayList<>();



}
