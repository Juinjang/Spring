package umc.th.juinjang.api.pencilAccount.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.pencilaccount.model.PencilAccount;

@Service
@RequiredArgsConstructor
public class PencilAccountService {

	private final PencilAccountFinder pencilAccountFinder;

	public Long getTotalPencilAmountByMember(Member member) {
		PencilAccount pencilAccount = pencilAccountFinder.findByMember(member);
		return pencilAccount.getTotalBalance();
	}
}
