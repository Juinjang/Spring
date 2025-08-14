package umc.th.juinjang.common.exception.handler;

import umc.th.juinjang.common.code.BaseErrorCode;
import umc.th.juinjang.common.exception.GeneralException;

public class PencilAccountHandler extends GeneralException {
	public PencilAccountHandler(BaseErrorCode code) {
		super(code);
	}
}
