package com.chrisgya.springsecurity.utils.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Christian.Gyaban on 02/17/2020.
 */

public class PasswordValidator implements ConstraintValidator<Password, String> {
    private final String regex = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[\\d])(?=.*?[\\W]).{4,35}$";

    @Override
    public void initialize(Password constraintAnnotation) {

    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (password == null) {
            return true;
        }
        return password.matches(this.regex);
    }
}
