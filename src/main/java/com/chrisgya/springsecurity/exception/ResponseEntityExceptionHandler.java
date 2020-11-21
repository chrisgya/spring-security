package com.chrisgya.springsecurity.exception;

import com.chrisgya.springsecurity.model.ErrorMessage;
import com.chrisgya.springsecurity.model.ValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
@ControllerAdvice
@ResponseBody
public class ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ErrorMessage handleBadRequestException(BadRequestException e, WebRequest req) {
        return new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(), e.getMessage(), req.getDescription(false));
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ErrorMessage handleNotFoundException(NotFoundException e, WebRequest req) {
        return new ErrorMessage(HttpStatus.NOT_FOUND.value(), new Date(), e.getMessage(), req.getDescription(false));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final List<ValidationError> ConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

        List<ValidationError> errors = new ArrayList<>();
        constraintViolations.stream()
                .forEach(constraintViolation -> {
                    String fieldName = null;
                    for (Path.Node node : constraintViolation.getPropertyPath()) {
                        fieldName = node.getName();
                    }
                    errors.add(new ValidationError(fieldName, constraintViolation.getMessage()));
                });

        return errors;
    }


}
