package umc.th.juinjang.domain.checklist.model;


import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.ChecklistHandler;

import java.util.Arrays;

public enum ChecklistQuestionCategory {
  DEADLINE(0), //기한
  LOCATION_CONDITION(1), // 입지여건
  PUBLIC_SPACE(2), // 공용공간
  INDOOR(3); //실내

  private final int value;

  ChecklistQuestionCategory(int value) {
    this.value = value;
  }

  // 숫자 리턴
  public int getValue() {
    return value;
  }

  public static ChecklistQuestionCategory find(int inputValue) {
    return Arrays.stream(ChecklistQuestionCategory.values())
            .filter(it -> it.value == inputValue)
            .findAny()
            .orElseThrow(() -> new ChecklistHandler(ErrorStatus.CHECKLIST_TYPE_ERROR));
  }
}
