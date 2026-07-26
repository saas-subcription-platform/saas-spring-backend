package com.saas.springbackend.subscription;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String errMesg) {
        super(errMesg);
    }
}
