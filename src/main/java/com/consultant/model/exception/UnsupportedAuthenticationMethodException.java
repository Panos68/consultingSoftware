package com.consultant.model.exception;

public class UnsupportedAuthenticationMethodException extends RuntimeException {

    public UnsupportedAuthenticationMethodException(String message) {
        super(message);
    }
}
