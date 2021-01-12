package com.chrisgya.springsecurity.utils.validations;

public class ValidationMessage {
    public static String PASSWORD_MISMATCH = "password and confirmPassword do not match";
    public static String NOT_FOUND = "%s not found";
    public static String ALREADY_TAKEN = "%s is already taken!";
    public static String CONFIRM_EMAIL = "please confirm your email";
    public static String ACCOUNT_DISABLED = "account is disabled. please contact support team for assistance";
    public static String ACCOUNT_LOCKED = "account is locked. account would be available on %s";
    public static String INVALID_TOKEN = "invalid token";
    public static String EXPIRED_TOKEN = "token has expired";
    public static String INVALID_USER_TOKEN = "token does not belong to a valid user";
    public static String INVALID_REFRESH_TOKEN = "invalid refresh token";
    public static String EXPIRED_REFRESH_TOKEN = "refresh token has expired";
}
