package umc.th.juinjang.event.subscriber;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum EventMessage {
	SIGN_UP_MESSAGE("주인장에 %s %d번째 유저 < %s >님이 생겼어요!"),
	FLAG_SHARED_NOTE_MESSAGE("< %d >번 유저가 [ %s ]의 사유로 < %d >번 유저의 < %d >번 공유 노트를 신고했습니다."),
	PAYMENT_COMPLETED_MESSAGE("<%d>번 유저 < %s >님이 %d개의 연필을 %d원에 결제했습니다. (상태: %s)");


	private final String message;
}
