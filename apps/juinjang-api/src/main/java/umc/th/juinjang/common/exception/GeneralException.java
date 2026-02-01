package umc.th.juinjang.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import umc.th.juinjang.common.code.BaseErrorCode;
import umc.th.juinjang.common.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }
}