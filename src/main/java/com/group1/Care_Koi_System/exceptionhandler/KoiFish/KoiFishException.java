package com.group1.Care_Koi_System.exceptionhandler.KoiFish;

import com.group1.Care_Koi_System.exceptionhandler.ErrorCode;

public class KoiFishException extends RuntimeException{

    private ErrorCode errorCode;
    public KoiFishException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
