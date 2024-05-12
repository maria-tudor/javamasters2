package com.example.javamasters2.controller;

import com.example.javamasters2.exceptions.BindingResultException;
import com.example.javamasters2.exceptions.ResourceAlreadyReportedException;
import com.example.javamasters2.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import static com.example.javamasters2.constants.ExceptionConstants.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handlerNotFoundException(Exception exception){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put("exception", exception);
        modelAndView.setViewName(NOT_FOUND_VIEW);
        return modelAndView;
    }

    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(BindingResultException.class)
    public ModelAndView handlerExpectationFailedException(Exception exception){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put("exception", exception);
        modelAndView.setViewName(BINDING_FAILED_VIEW);
        return modelAndView;
    }

    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    @ExceptionHandler(ResourceAlreadyReportedException.class)
    public ModelAndView handlerAlreadyReportesException(Exception exception){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put("exception", exception);
        modelAndView.setViewName(ALREADY_REPORTED_VIEW);
        return modelAndView;
    }

}
