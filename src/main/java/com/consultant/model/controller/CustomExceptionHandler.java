package com.consultant.model.controller;

import com.consultant.model.dto.ErrorDTO;
import com.consultant.model.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    // TODO [aw] rename these and merge NotFound and NoMatch
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handle(
            NotFoundException ex, WebRequest request) {
        ErrorDTO error = new ErrorDTO(404, ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(NoMatchException.class)
    protected ResponseEntity<Object> handle(
            NoMatchException ex, WebRequest request) {
        ErrorDTO error = new ErrorDTO(404, ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(UnsupportedAuthenticationMethodException.class)
    protected ResponseEntity<Object> handle(
            UnsupportedAuthenticationMethodException ex, WebRequest request) {
        ErrorDTO error = new ErrorDTO(400, ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

//    @ExceptionHandler(ForbiddenException.class)
//    protected ResponseEntity<Object> handle(
//            ForbiddenException ex, WebRequest request) {
//        ErrorDTO error = new ErrorDTO(403, ex.getMessage());
//        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
//    }

    @ExceptionHandler(EntityAlreadyExists.class)
    protected ResponseEntity<Object> handle(
            EntityAlreadyExists ex, WebRequest request) {
        ErrorDTO error = new ErrorDTO(409, ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(EmailMissingException.class)
    protected ResponseEntity<Object> handle(
            EmailMissingException ex, WebRequest request) {
        ErrorDTO error = new ErrorDTO(500, ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handle(
            Exception ex, WebRequest request) {
        ErrorDTO error = new ErrorDTO(500, ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
