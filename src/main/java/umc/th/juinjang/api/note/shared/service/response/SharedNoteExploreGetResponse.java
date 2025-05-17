package umc.th.juinjang.api.note.shared.service.response;

import java.util.List;
import java.util.Map;
import java.util.Set;

import umc.th.juinjang.api.note.shared.service.util.SharedNotesTimeAgoFormatter;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.note.shared.model.SharedNote;

public record SharedNoteExploreGetResponse(
	long totalResults,
	List<SharedNoteExploreResponse> notes
) {

	public static SharedNoteExploreGetResponse of(long totalResults, List<SharedNote> sharedNotes,
		Set<Long> isPurchaseMap, Set<Long> likedNotes, Map<Long, Long> viewCountMap
	) {
		return new SharedNoteExploreGetResponse(totalResults,
			sharedNotes.stream()
				.map(it -> SharedNoteExploreResponse.of(
					it,
					it.getLimjang(),
					isPurchaseMap.contains(it.getSharedNoteId()),
					likedNotes.contains(it.getSharedNoteId()),
					viewCountMap.get(it.getSharedNoteId()),
					it.getMember()))
				.toList());
	}
}

record SharedNoteExploreResponse(
	Long sharedNoteId,
	LimjangPropertyType propertyType,
	LimjangPriceType priceType,
	String buildingName,
	String imageUrl,
	Boolean isPurchase,
	Boolean isLiked,
	String rate,
	String price,
	String monthlyRent,
	Integer pyong,
	String floor,
	String address,
	String ownerImageUrl,
	String ownerNickname,
	String timeAge,
	Long viewCount
) {

	public static SharedNoteExploreResponse of(SharedNote sharedNote, Limjang note, boolean isPurchase, boolean isLiked,
		Long viewCount, Member member) {
		return new SharedNoteExploreResponse(
			sharedNote.getSharedNoteId(),
			note.getPropertyType(),
			note.getPriceType(),
			sharedNote.getBuildingName(),
			sharedNote.isImageShared() ? note.getDefaultImage() : null,
			isPurchase,
			isLiked,
			note.getReport() == null ? null : note.getReport().getTotalRate().toString(),
			note.getLimjangPrice().getPrice(note.getPriceType(), note.getPurpose()),
			note.getPriceType() == LimjangPriceType.MONTHLY_RENT ? note.getLimjangPrice().getMonthlyRent() : null,
			note.getPyong(),
			note.getFloor(),
			note.getAddressEntity().getShortAddress(),
			member.getImageUrl(),
			member.getNickname(),
			SharedNotesTimeAgoFormatter.getTimeAge(sharedNote.getCreatedAt()),
			viewCount
		);
	}
}
