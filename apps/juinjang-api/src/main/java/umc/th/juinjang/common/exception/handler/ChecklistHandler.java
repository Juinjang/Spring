package umc.th.juinjang.common.exception.handler;

import umc.th.juinjang.common.code.BaseErrorCode;
import umc.th.juinjang.common.exception.GeneralException;

public class ChecklistHandler extends GeneralException {
    public ChecklistHandler(BaseErrorCode code) {
      super(code);
    }
}
