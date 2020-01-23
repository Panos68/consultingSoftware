package com.consultant.model.exception;

public class EntityAlreadyExists extends RuntimeException {

    public EntityAlreadyExists(final String msg) {
        super(msg);
    }
}
