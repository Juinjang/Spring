package umc.th.juinjang.api.image.service;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.domain.image.model.Image;
import umc.th.juinjang.domain.image.repository.ImageRepository;
import umc.th.juinjang.domain.limjang.model.Limjang;

@Component
@RequiredArgsConstructor
public class ImageFinder {

	private final ImageRepository imageRepository;

	public List<Image> findAllFirstCreatedImagePerNote(List<Limjang> notes) {
		return imageRepository.findAllFirstCreatedImagePerNote(notes);
	}
}
