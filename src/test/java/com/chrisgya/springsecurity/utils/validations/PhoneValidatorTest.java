package com.chrisgya.springsecurity.utils.validations;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PhoneValidatorTest {

    @Test
    void isValid() {
        PhoneValidator phoneValidator = new PhoneValidator();
        assertTrue(phoneValidator.isValid("07089960733", null));
        assertTrue(phoneValidator.isValid("070i89960733", null));
        assertFalse(phoneValidator.isValid("070899607334", null));
        assertTrue(phoneValidator.isValid("+2347089960733", null));
        assertFalse(phoneValidator.isValid("+2347089thiswillberemoved960733", null));
        assertFalse(phoneValidator.isValid("+23470899607334", null));
    }
}