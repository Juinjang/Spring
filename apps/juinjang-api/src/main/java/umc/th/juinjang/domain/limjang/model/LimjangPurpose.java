package umc.th.juinjang.domain.limjang.model;

import java.util.Arrays;

import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.LimjangHandler;

public enum LimjangPurpose {

	INVESTMENT(0),  // 투자 목적
	RESIDENTIAL_PURPOSE(1); // 거주 목적, 직접 입주

	private final int value;

	LimjangPurpose(int value) {
		this.value = value;
	}

	// 숫자 리턴
	public int getValue() {
		return value;
	}

	public static LimjangPurpose find(int inputValue) {
		return Arrays.stream(LimjangPurpose.values())
			.filter(it -> it.value == inputValue)
			.findAny()
			.orElseThrow(() -> new LimjangHandler(ErrorStatus.LIMJANG_POST_TYPE_ERROR));
	}

}
