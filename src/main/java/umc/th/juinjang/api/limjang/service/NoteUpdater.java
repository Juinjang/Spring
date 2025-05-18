package umc.th.juinjang.api.limjang.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.repository.LimjangRepository;

@Component
@RequiredArgsConstructor
public class NoteUpdater {

	private final LimjangRepository limjangRepository;

	public void save(Limjang limjang) {
		limjangRepository.save(limjang);
	}
}
