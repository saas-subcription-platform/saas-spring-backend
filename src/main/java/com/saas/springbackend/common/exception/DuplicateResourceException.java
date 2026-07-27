package com.saas.springbackend.common.exception;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String errMesg) {
        super(errMesg);
    }
}
