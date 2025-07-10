package umc.th.juinjang.api.note.shared.service.response;

import java.time.format.DateTimeFormatter;
import java.util.List;

import umc.th.juinjang.domain.image.model.Image;
import umc.th.juinjang.domain.limjang.model.Address;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.model.LimjangPriceType;
import umc.th.juinjang.domain.limjang.model.LimjangPropertyType;
import umc.th.juinjang.domain.limjang.model.LimjangPurpose;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.note.shared.model.SharedNote;

public record SharedNoteGetResponse(
	boolean isBuyer,
	boolean isImageShared,
	Long requiredPencils,
	Integer imageCount,
	Integer checkedCount,
	Integer reviewLength,
	String buildingName,
	LimjangPurpose limjangPurpose,
	LimjangPropertyType propertyType,
	LimjangPriceType priceType,
	Integer buyerCount,
	List<String> images,
	String address,
	String addressShort,
	String price,
	String monthlyRent,
	boolean isLiked,
	Long likedCount,
	String period,
	String updatedAt,
	Long viewCount,
	String floor,
	int pyong,
	String ownerProfileUrl,
	String ownerNickname,
	String ownerProfileBio
) {
	public static SharedNoteGetResponse ofNotPurchased(
		boolean isBuyer,
		Limjang limjang,
		Address address,
		SharedNote sharedNote,
		Member member,
		Integer buyerCount,
		boolean isLiked,
		Long viewCount) {
		return new SharedNoteGetResponse(
			isBuyer,
			sharedNote.isImageShared(),
			sharedNote.getPrice(),
			limjang.getImageList().size(),
			limjang.getAnswerList().size(),
			sharedNote.getReview().length(),
			sharedNote.getBuildingName(),
			limjang.getPurpose(),
			limjang.getPropertyType(),
			limjang.getPriceType(),
			buyerCount,
			findImagesUrlBySharingStatus(sharedNote.isImageShared(), limjang, 2),
			address.getFullAddress(),
			address.getShortAddress(),
			limjang.getLimjangPrice().getPrice(limjang.getPriceType(), limjang.getPurpose()),
			limjang.getPriceType() == LimjangPriceType.MONTHLY_RENT ? limjang.getLimjangPrice().getMonthlyRent() :
				null,
			isLiked,
			sharedNote.getLikeCount(),
			sharedNote.getPullPeriod(),
			null,
			viewCount,
			limjang.getFloor(),
			limjang.getPyong(),
			member.getImageUrl(),
			member.getNickname(),
			member.getIntroduction()

		);
	}

	public static SharedNoteGetResponse ofPurchased(
		boolean isBuyer,
		Limjang limjang,
		Address address,
		SharedNote sharedNote,
		Member member,
		Integer buyerCount,
		boolean isLiked,
		Long viewCount) {
		return new SharedNoteGetResponse(
			isBuyer,
			sharedNote.isImageShared(),
			null,
			null,
			null,
			null,
			sharedNote.getBuildingName(),
			limjang.getPurpose(),
			limjang.getPropertyType(),
			limjang.getPriceType(),
			buyerCount,
			findImagesUrlBySharingStatus(sharedNote.isImageShared(), limjang, 3),
			address.getFullAddress(),
			address.getShortAddress(),
			limjang.getLimjangPrice().getPrice(limjang.getPriceType(), limjang.getPurpose()),
			limjang.getPriceType() == LimjangPriceType.MONTHLY_RENT ? limjang.getLimjangPrice().getMonthlyRent() :
				null,
			isLiked,
			sharedNote.getLikeCount(),
			sharedNote.getPullPeriod(),
			sharedNote.getUpdatedAt().format(DateTimeFormatter.ofPattern("yy.MM.dd")),
			viewCount,
			limjang.getFloor(),
			limjang.getPyong(),
			member.getImageUrl(),
			member.getNickname(),
			member.getIntroduction()

		);
	}

	private static List<String> findImagesUrlBySharingStatus(boolean isImageShared, Limjang limjang, int maxSize) {
		if (isImageShared) {
			return limjang.getImageList().stream().map(Image::getImageUrl).limit(maxSize).toList();
		}
		return List.of();
	}
}
