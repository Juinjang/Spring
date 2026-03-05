package umc.th.juinjang.api.pencilAccount.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import umc.th.juinjang.domain.pencilaccount.model.PencilAccount;
import umc.th.juinjang.domain.pencilaccount.repository.PencilAccountRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class PencilAccountUpdater {

	private final PencilAccountRepository pencilAccountRepository;

	public void save(PencilAccount pencilAccount) {
		pencilAccountRepository.save(pencilAccount);
	}

}
