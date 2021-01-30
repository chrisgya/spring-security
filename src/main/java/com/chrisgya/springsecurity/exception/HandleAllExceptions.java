package com.chrisgya.springsecurity.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.chrisgya.springsecurity.model.ErrorMessage;
import com.chrisgya.springsecurity.model.ValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.chrisgya.springsecurity.utils.validations.ValidationMessage.PASSWORD_MISMATCH;

@Slf4j
@ControllerAdvice
public class HandleAllExceptions extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ForbiddenException.class)
    public final ResponseEntity<?> handleAccessDeniedException(ForbiddenException e, WebRequest req) {
        var response = new ErrorMessage(HttpStatus.FORBIDDEN.value(), new Date(), e.getMessage(), req.getDescription(false));
        return new ResponseEntity(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<?> handleBadRequestException(BadRequestException e, WebRequest req) {
        log.error("BadRequestException:: {}", e);

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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<?> dataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("DataIntegrityViolationException:: {}", e);

        var message = e.getCause().getCause().getMessage().split("=")[1];
        var response = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(), message, null);
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ValidationError> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            String fieldName;
            if (errorMessage.equals(PASSWORD_MISMATCH)) {
                fieldName = "confirmPassword";
            } else {
                fieldName = ((FieldError) error).getField();
            }
            errors.add(new ValidationError(fieldName, errorMessage));
        });

        return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public final ResponseEntity<?> handleTokenExpiredExceptionException(Exception e) {
        log.error("TokenExpiredException:: {}", e);
        var response = new ErrorMessage(HttpStatus.UNAUTHORIZED.value(), new Date(), e.getMessage(), "");
        return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<?> handleUnhandledException(Exception e) {
        log.error("Exception:: {}", e);
        var message = e.getMessage();
        var httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        if(e instanceof UsernameNotFoundException){
            message = "invalid token";
            httpStatus = HttpStatus.UNAUTHORIZED;
        }
        var response = new ErrorMessage(httpStatus.value(), new Date(), message, "");
        return new ResponseEntity(response, httpStatus);
    }

}
