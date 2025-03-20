package umc.th.juinjang.domain.report.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import lombok.*;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.common.BaseEntity;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Report extends BaseEntity {

  @Id
  @Column(name="report_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reportId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "limjang_id", referencedColumnName = "limjang_id")
  private Limjang limjangId;

  @Column(nullable = false)
  private String indoorKeyword;

  @Column(nullable = false)
  private String publicSpaceKeyword;

  @Column(nullable = false)
  private String locationConditionsKeyword;

  @Column(nullable = false)
  private Float indoorRate;

  @Column(nullable = false)
  private Float publicSpaceRate;

  @Column(nullable = false)
  private Float locationConditionsRate;

  @Column(nullable = false)
  private Float totalRate;
}
