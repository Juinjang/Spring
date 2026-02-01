package umc.th.juinjang.api.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.th.juinjang.common.ExceptionHandler;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.domain.withdraw.model.Withdraw;
import umc.th.juinjang.domain.withdraw.model.WithdrawReason;
import umc.th.juinjang.domain.withdraw.repository.WithdrawRepository;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WithdrawService {

    private final WithdrawRepository withdrawRepository;

    public void addWithdrawReason(List<String> withdrawReasons) {
        for(String reason : withdrawReasons) {
            WithdrawReason withdrawReason = WithdrawReason.valueOf(reason.toUpperCase());

            Withdraw withdraw = withdrawRepository.findByWithdrawReason(withdrawReason)
                    .orElseThrow(() -> new ExceptionHandler(ErrorStatus.WITHDRAW_REASON_NOT_FOUND));

            withdraw.updateCount(withdraw.getCount() + 1);
            withdrawRepository.save(withdraw);
        }
    }
}
