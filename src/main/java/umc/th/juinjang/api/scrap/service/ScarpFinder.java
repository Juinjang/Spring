package umc.th.juinjang.api.scrap.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.scrap.model.Scrap;
import umc.th.juinjang.domain.scrap.repository.ScrapRepository;

@Service
@RequiredArgsConstructor
public class ScarpFinder {

	private final ScrapRepository scrapRepository;

	public List<Scrap> findAllByNoteId(List<Limjang> notes) {
		return scrapRepository.findAllByLimjangIdIn(notes);
	}
}
