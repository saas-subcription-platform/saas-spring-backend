package com.saas.springbackend.common.handler;

import com.saas.springbackend.common.dto.ErrorResponseDto;
import com.saas.springbackend.common.exception.CompanyAlreadyExistsException;
import com.saas.springbackend.common.exception.EmailAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleEmailAlreadyExists(
            EmailAlreadyExistsException ex,
            HttpServletRequest request) {

        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CompanyAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleCompanyAlreadyExists(
            CompanyAlreadyExistsException ex,
            HttpServletRequest request) {

        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

//    Other exceptions goes here...


//    Generic Exception Handler
@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponseDto> handleException(
        Exception ex,
        HttpServletRequest request) {

    ErrorResponseDto error = new ErrorResponseDto(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI()
    );

    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
}
}