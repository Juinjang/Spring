package umc.th.juinjang.domain.withdraw.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import umc.th.juinjang.domain.common.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Withdraw extends BaseEntity {

    @Id
    @Column(name="withdraw_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long withdrawId;

    @Column(name="reason")
    @Enumerated(EnumType.STRING)
    private WithdrawReason withdrawReason;

    @ColumnDefault("0")
    private Long count;

    public void updateCount(Long count) {
        this.count = count;
    }
}
