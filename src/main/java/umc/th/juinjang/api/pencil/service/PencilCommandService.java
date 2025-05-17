package umc.th.juinjang.api.pencil.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.pencil.acquired.model.AcquiredPencil;

@Service
@RequiredArgsConstructor
public class PencilCommandService {

	private final AcquiredPencilFinder acquiredPencilFinder;

	@Transactional
	public Boolean markAcquiredPencilAsRead(Long acquiredPencilId) {
		AcquiredPencil acquiredPencil = acquiredPencilFinder.findById(acquiredPencilId);

		if (acquiredPencil == null) {
			throw new EntityNotFoundException("AcquiredPencil not found with id: " + acquiredPencilId);
		}

		acquiredPencil.updateIsReadAsTrue();
		return true;
	}
}
