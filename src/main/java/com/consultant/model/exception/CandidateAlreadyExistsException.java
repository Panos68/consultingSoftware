package com.consultant.model.exception;

public class CandidateAlreadyExistsException extends RuntimeException {

    public CandidateAlreadyExistsException(final String msg) {
        super(msg);
    }
}
