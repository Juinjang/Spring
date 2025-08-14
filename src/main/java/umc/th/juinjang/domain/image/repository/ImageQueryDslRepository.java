package umc.th.juinjang.domain.image.repository;

import java.util.List;

import umc.th.juinjang.domain.image.model.Image;
import umc.th.juinjang.domain.limjang.model.Limjang;

public interface ImageQueryDslRepository {

	List<Image> findAllFirstCreatedImagePerNote(List<Limjang> limjangs);
}
