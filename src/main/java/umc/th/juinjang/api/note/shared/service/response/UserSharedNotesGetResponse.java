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

public record UserSharedNotesGetResponse(
	List<UsersSharedNoteResponse> notes

) {

	public static UserSharedNotesGetResponse ofLiked(List<SharedNote> sharedNotes, Set<Long> isPurchaseMap,
		Map<Long, Long> viewCountMap) {
		return new UserSharedNotesGetResponse(sharedNotes.stream().map(it -> UsersSharedNoteResponse.of(
			it,
			it.getLimjang(),
			isPurchaseMap.contains(it.getSharedNoteId()),
			true,
			viewCountMap.get(it.getSharedNoteId()),
			it.getMember()
		)).toList());
	}

	public static UserSharedNotesGetResponse ofShared(Member member, List<SharedNote> sharedNotes,
		Set<Long> likedNotes, Map<Long, Long> viewCountMap) {
		return new UserSharedNotesGetResponse(sharedNotes.stream().map(it -> UsersSharedNoteResponse.of(
			it,
			it.getLimjang(),
			false,
			likedNotes.contains(it.getSharedNoteId()),
			viewCountMap.get(it.getSharedNoteId()),
			member
		)).toList());
	}

	public static UserSharedNotesGetResponse ofOwned(List<SharedNote> sharedNotes,
		Set<Long> likedNotes, Map<Long, Long> viewCountMap) {
		return new UserSharedNotesGetResponse(sharedNotes.stream().map(it -> UsersSharedNoteResponse.of(
			it,
			it.getLimjang(),
			true,
			likedNotes.contains(it.getSharedNoteId()),
			viewCountMap.get(it.getSharedNoteId()),
			it.getMember()
		)).toList());
	}
}

record UsersSharedNoteResponse(
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
	Long viewCount) {

	static UsersSharedNoteResponse of(SharedNote sharedNote, Limjang note, boolean isPurchase, boolean isLiked,
		Long viewCount, Member member) {
		return new UsersSharedNoteResponse(
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
