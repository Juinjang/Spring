package umc.th.juinjang.domain.flag.model;

import lombok.Getter;

@Getter
public enum FlagSharedNoteStatus {
	RECEIVED("접수"),
	REVIEWED("관리자가 확인"),
	RESOLVED("조치 취함");

	private final String description;

	FlagSharedNoteStatus(String description) {
		this.description = description;
	}

}
