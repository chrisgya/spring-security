package com.chrisgya.springsecurity.utils;

import java.security.SecureRandom;

public class RandomUtil {
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String randomAlphaNumeric(int count) {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder builder = new StringBuilder(count);
        while (count-- != 0) {
            int character = secureRandom.nextInt(ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}
