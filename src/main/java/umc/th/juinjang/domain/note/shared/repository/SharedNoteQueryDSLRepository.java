package umc.th.juinjang.domain.note.shared.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import umc.th.juinjang.api.note.shared.controller.ExploreSortType;
import umc.th.juinjang.api.note.shared.controller.NoteType;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.note.shared.model.SharedNote;

public interface SharedNoteQueryDSLRepository {

	Page<SharedNote> findSharedNoteInExployer(List<String> code, ExploreSortType sort,
		LimjangPropertyType propertyType, LimjangPriceType priceType, String keyword, Pageable pageable);

	List<SharedNote> findUserSharedNotes(Member member, NoteType noteType, LimjangPropertyType propertyType,
		LimjangPriceType priceType, String keyword);
}
