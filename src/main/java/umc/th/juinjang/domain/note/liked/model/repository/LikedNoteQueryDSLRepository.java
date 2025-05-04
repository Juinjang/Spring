package umc.th.juinjang.domain.note.liked.model.repository;

import java.util.List;

import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.note.liked.model.LikedNote;

public interface LikedNoteQueryDSLRepository {

	List<LikedNote> findAllByMemberAndDynamic(Member user, LimjangPropertyType propertyType,
		LimjangPriceType priceType, String keyword);
}
