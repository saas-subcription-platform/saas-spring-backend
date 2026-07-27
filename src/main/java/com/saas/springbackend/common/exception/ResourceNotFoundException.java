package com.saas.springbackend.common.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String errMesg) {
        super(errMesg);
    }
}
