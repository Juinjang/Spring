package umc.th.juinjang.common.exception.handler;

import umc.th.juinjang.common.code.BaseErrorCode;
import umc.th.juinjang.common.exception.GeneralException;

public class SharedNoteHandler extends GeneralException {
	public SharedNoteHandler(BaseErrorCode code) {
		super(code);
	}
}
