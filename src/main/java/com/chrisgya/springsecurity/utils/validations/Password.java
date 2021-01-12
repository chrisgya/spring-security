package com.chrisgya.springsecurity.utils.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by Christian.Gyaban on 02/17/2020.
 */
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface Password {
    String message() default "Password needs to have at least 1 lower case, 1 uppercase, 1 number, 1 special character, and must be at least 4 characters but no more than 35";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
