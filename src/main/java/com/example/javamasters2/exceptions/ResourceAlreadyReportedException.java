package com.example.javamasters2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
@ControllerAdvice
public class ResourceAlreadyReportedException extends RuntimeException {
    public ResourceAlreadyReportedException() {
    }
    public ResourceAlreadyReportedException(String message) {
        super(message);
    }
    public ResourceAlreadyReportedException(String message, Throwable
            throwable) {
        super(message, throwable);
    }
}