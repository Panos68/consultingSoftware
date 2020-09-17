package com.consultant.model.controller;

import com.consultant.model.exception.*;
import com.nimbusds.oauth2.sdk.ErrorObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    // TODO [aw] rename these and merge NotFound and NoMatch
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handle(
            NotFoundException ex, WebRequest request) {
        ErrorObject error = new ErrorObject("404", ex.getMessage(), 404);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(NoMatchException.class)
    protected ResponseEntity<Object> handle(
            NoMatchException ex, WebRequest request) {
        ErrorObject error = new ErrorObject("404", ex.getMessage(), 404);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(UnsupportedAuthenticationMethodException.class)
    protected ResponseEntity<Object> handle(
            UnsupportedAuthenticationMethodException ex, WebRequest request) {
        ErrorObject error = new ErrorObject("400", ex.getMessage(), 400);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ForbiddenException.class)
    protected ResponseEntity<Object> handle(
            ForbiddenException ex, WebRequest request) {
        ErrorObject error = new ErrorObject("403", ex.getMessage(), 403);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(EntityAlreadyExists.class)
    protected ResponseEntity<Object> handle(
            EntityAlreadyExists ex, WebRequest request) {
        ErrorObject error = new ErrorObject("409", ex.getMessage(), 409);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(EmailMissingException.class)
    protected ResponseEntity<Object> handle(
            EmailMissingException ex, WebRequest request) {
        ErrorObject error = new ErrorObject("500", ex.getMessage(), 500);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handle(
            Exception ex, WebRequest request) {
        ErrorObject error = new ErrorObject("500", ex.getMessage(), 500);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
