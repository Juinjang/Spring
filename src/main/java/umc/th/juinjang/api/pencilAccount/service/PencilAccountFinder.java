package umc.th.juinjang.api.pencilAccount.service;

import static umc.th.juinjang.common.code.status.ErrorStatus.*;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.common.exception.handler.PencilAccountHandler;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.pencilaccount.model.PencilAccount;
import umc.th.juinjang.domain.pencilaccount.repository.PencilAccountRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class PencilAccountFinder {

	private final PencilAccountRepository pencilAccountRepository;

	protected PencilAccount findByMember(Member member) {
		return pencilAccountRepository.findByMember(member).orElseThrow(
			() -> {
				log.error("[PENCIL_ACCOUNT]");
				return new PencilAccountHandler(PENCIL_ACCOUNT_NOT_FOUND);
			}
		);
	}

	public PencilAccount findByMemberWithLock(Member member) {
		return pencilAccountRepository.findByMemberWithLock(member)
			.orElseThrow(() -> new PencilAccountHandler(PENCIL_ACCOUNT_NOT_FOUND));
	}

}
