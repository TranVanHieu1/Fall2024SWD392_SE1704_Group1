package com.group1.Care_Koi_System.exceptionhandler.Account;

import com.group1.Care_Koi_System.exceptionhandler.ErrorCode;

public class AccountException extends RuntimeException{
    private ErrorCode errorCode;

    public AccountException(ErrorCode errorCode) {
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
