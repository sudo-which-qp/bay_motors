package com.baymotors.exceptions;

public class BayMotorsException extends Exception{
    private final ErrorCode errorCode;

    public enum ErrorCode {
        INVALID_INPUT,
        UNAUTHORIZED_ACCESS,
        CUSTOMER_NOT_FOUND,
        VEHICLE_NOT_FOUND,
        MECHANIC_UNAVAILABLE,
        TASK_ERROR,
        SYSTEM_ERROR
    }

    public BayMotorsException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BayMotorsException(String message, ErrorCode errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
