package umc.th.juinjang.common.exception.handler;

import umc.th.juinjang.common.code.BaseErrorCode;
import umc.th.juinjang.common.exception.GeneralException;

public class AppleHandler extends GeneralException {
	public AppleHandler(BaseErrorCode code) {
		super(code);
	}

}
