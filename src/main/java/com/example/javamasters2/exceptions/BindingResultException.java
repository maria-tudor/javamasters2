package com.example.javamasters2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
@ControllerAdvice
public class BindingResultException extends RuntimeException {
    public BindingResultException() {
    }
    public BindingResultException(String message) {
        super(message);
    }
    public BindingResultException(String message, Throwable
            throwable) {
        super(message, throwable);
    }
}
