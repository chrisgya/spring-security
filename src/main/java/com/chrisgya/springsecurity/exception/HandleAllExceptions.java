package com.chrisgya.springsecurity.exception;

import com.chrisgya.springsecurity.model.ErrorMessage;
import com.chrisgya.springsecurity.model.ValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@ControllerAdvice
public class HandleAllExceptions extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<?> handleBadRequestException(BadRequestException e, WebRequest req) {
        var response = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(), e.getMessage(), req.getDescription(false));
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public final ResponseEntity<?> handleAuthenticationException(org.springframework.security.access.AccessDeniedException e, WebRequest req) {
        var response = new ErrorMessage(HttpStatus.UNAUTHORIZED.value(), new Date(), e.getMessage(), req.getDescription(false));
        return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<?> handleNotFoundException(NotFoundException e, WebRequest req) {
        var response = new ErrorMessage(HttpStatus.NOT_FOUND.value(), new Date(), e.getMessage(), req.getDescription(false));
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ValidationError> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(new ValidationError(fieldName, errorMessage));
        });

        return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<?> handleUnhandledException(Exception e) {
        var response = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(), e.getMessage(), "");
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
