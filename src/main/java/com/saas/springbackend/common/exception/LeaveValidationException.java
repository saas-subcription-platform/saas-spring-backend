package com.saas.springbackend.common.exception;

public class LeaveValidationException extends RuntimeException {

    public LeaveValidationException(String message) {
        super(message);
    }
}