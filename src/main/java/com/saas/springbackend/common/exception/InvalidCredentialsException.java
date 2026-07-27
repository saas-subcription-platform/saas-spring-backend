package com.saas.springbackend.common.exception;

public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException(String message) {super(message);}
}
