package com.saas.springbackend.subscription;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String errMesg) {
        super(errMesg);
    }
}
