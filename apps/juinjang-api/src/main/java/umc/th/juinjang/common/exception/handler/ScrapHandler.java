package umc.th.juinjang.common.exception.handler;

import umc.th.juinjang.common.code.BaseErrorCode;
import umc.th.juinjang.common.exception.GeneralException;

public class ScrapHandler extends GeneralException {
  public ScrapHandler(BaseErrorCode code) {
    super(code);
  }
}
