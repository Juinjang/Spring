package umc.th.juinjang.api.pencil.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredPencil;
import umc.th.juinjang.domain.pencil.acquired.repository.AcquiredPencilRepository;

@Component
@RequiredArgsConstructor
public class AcquiredPencilUpdater {

	private final AcquiredPencilRepository acquiredPencilRepository;

	public void save(AcquiredPencil acquiredPencil) {
		acquiredPencilRepository.save(acquiredPencil);
	}

}
