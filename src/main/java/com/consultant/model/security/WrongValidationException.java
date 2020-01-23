package com.consultant.model.security;

public class WrongValidationException extends RuntimeException {

    public WrongValidationException(final String msg) {
        super(msg);
    }
}
