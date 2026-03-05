package umc.th.juinjang.common.exception.handler;

import umc.th.juinjang.common.code.BaseErrorCode;
import umc.th.juinjang.common.exception.GeneralException;

public class MemberHandler extends GeneralException {
  public MemberHandler(BaseErrorCode code) {
    super(code);
  }
}
