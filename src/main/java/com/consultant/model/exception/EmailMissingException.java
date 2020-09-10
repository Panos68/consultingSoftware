package com.consultant.model.exception;

public class EmailMissingException extends RuntimeException {

    public EmailMissingException(String message) {
        super(message);
    }
}
