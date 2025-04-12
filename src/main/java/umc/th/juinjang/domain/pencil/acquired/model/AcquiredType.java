package umc.th.juinjang.domain.pencil.acquired.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AcquiredType {
	SHARE("SHARE"),    // 공유 시, 연필을 획득한 경우
	SOLD("Sold");    // 판매를 통한, 연필 획득
	// TODO : 추후에 추가로 생길 수도 있음.
	private final String value;

}
