package umc.th.juinjang.common.exception.handler;

import umc.th.juinjang.common.code.BaseErrorCode;
import umc.th.juinjang.common.exception.GeneralException;

public class LimjangHandler extends GeneralException {
    public LimjangHandler(BaseErrorCode code) {
      super(code);
    }
}
