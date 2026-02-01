package umc.th.juinjang.domain.checklist.model;

import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.ChecklistHandler;

import java.util.Arrays;

public enum ChecklistQuestionVersion {

  // 원룸용 체크리스트
  ONE_ROOM(0),
  // 임장용 체크리스트
  LIMJANG(1);

  private final int value;

  ChecklistQuestionVersion(int value) {
    this.value = value;
  }

  // 숫자 리턴
  public int getValue() {
    return value;
  }

  public static ChecklistQuestionVersion find(int inputValue) {
    return Arrays.stream(ChecklistQuestionVersion.values())
            .filter(it -> it.value == inputValue)
            .findAny()
            .orElseThrow(() -> new ChecklistHandler(ErrorStatus.CHECKLIST_TYPE_ERROR));
  }
}
