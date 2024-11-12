package com.baymotors.exceptions;

public class AuthException extends BayMotorsException{
    public AuthException(String message) {
        super(message, ErrorCode.UNAUTHORIZED_ACCESS);
    }
}
