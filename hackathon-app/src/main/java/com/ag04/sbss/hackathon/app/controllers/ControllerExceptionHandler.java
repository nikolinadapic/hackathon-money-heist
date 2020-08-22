package com.ag04.sbss.hackathon.app.controllers;

import com.ag04.sbss.hackathon.app.services.exception.MethodNotAllowedException;
import com.ag04.sbss.hackathon.app.services.exception.RequestDeniedException;
import com.ag04.sbss.hackathon.app.services.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(MethodNotAllowedException.class)
    @ResponseBody
    public String handleMethodNotAllowedException(Exception exception) {
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RequestDeniedException.class)
    @ResponseBody
    public String handleRequestDeniedException(Exception exception) {
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public String handleResourceNotFoundException(Exception exception) {
        return exception.getMessage();
    }
}
