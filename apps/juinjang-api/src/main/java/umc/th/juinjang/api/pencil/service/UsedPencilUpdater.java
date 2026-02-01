package umc.th.juinjang.api.pencil.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.pencil.used.model.UsedPencil;
import umc.th.juinjang.domain.pencil.used.repository.UsedPencilRepository;

@Component
@RequiredArgsConstructor
public class UsedPencilUpdater {

	private final UsedPencilRepository usedPencilRepository;

	public void save(UsedPencil usedPencil) {
		usedPencilRepository.save(usedPencil);
	}
}
