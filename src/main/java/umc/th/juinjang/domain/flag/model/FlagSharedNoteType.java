package umc.th.juinjang.domain.flag.model;

import lombok.Getter;

@Getter
public enum FlagSharedNoteType {
	FALSE_INFORMATION("부정확한 정보 제공"),
	ILLEGAL_BROKERING("거래 유도/불법 중개 행위"),
	INAPPROPRIATE_CONTENT("부적절한 내용 포함"),
	PERSONAL_INFORMATION_LEAK("개인정보 노출"),
	INFRINGEMENT("타인의 권리 침해"),
	SPAM("반복성 스팸/도배"),
	ETC("기타");

	private final String description;

	FlagSharedNoteType(String description) {
		this.description = description;
	}
}
