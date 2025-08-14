package umc.th.juinjang.common.exception.handler;

import umc.th.juinjang.common.code.BaseErrorCode;
import umc.th.juinjang.common.exception.GeneralException;

public class S3Handler extends GeneralException {
  public S3Handler(BaseErrorCode code) {
    super(code);
  }
}
