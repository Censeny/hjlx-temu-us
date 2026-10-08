package com.hjlx.temu.exception;

/**
 * 业务异常。
 */
public class BusinessException extends RuntimeException {

    private final Object errorCode;

    public BusinessException(String message) {
        super(message);
        this.errorCode = "BUSINESS_ERROR";
    }

    public BusinessException(Object errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public Object getErrorCode() {
        return errorCode;
    }
}
