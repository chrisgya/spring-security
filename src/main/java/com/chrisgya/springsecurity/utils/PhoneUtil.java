package com.chrisgya.springsecurity.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

@Slf4j
public class PhoneUtil {
    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    private static final String REGION_CODE_SEPARATOR = "|";
    private static final String DEFAULT_REGION_CODE = "NG";
    private static final String E164_PREFIX = "+";

    public static String normalize(String phone) {
        if (StringUtils.isBlank(phone) || isNormalized(phone)) {
            return phone;
        }
        try {
            String[] number = split(phone);
            return normalize(number[0], number[1]);
        } catch (IllegalArgumentException e) {
            log.error("Exception normalizing phone number", e);
            return "";
        }
    }

    public static String normalize(String region, String phone) {
        try {
            Assert.hasText(region, "region cannot be empty");
            Assert.hasText(phone, "phone cannot be empty");
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(phone, region);
            return phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (IllegalArgumentException | NumberParseException e) {
            log.error("Exception normalizing phone number", e);
            return "";
        }
    }

    public static boolean isValid(String phone) {
        try {
            String[] number = split(phone);
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(number[1], number[0]);
            PhoneNumberUtil.PhoneNumberType phoneNumberType = phoneNumberUtil.getNumberType(phoneNumber);
            return phoneNumberType == PhoneNumberUtil.PhoneNumberType.FIXED_LINE ||
                    phoneNumberType == PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE ||
                    phoneNumberType == PhoneNumberUtil.PhoneNumberType.MOBILE;
        } catch (IllegalArgumentException | NumberParseException e) {
            log.error("Exception validating phone number", e);
            return false;
        }
    }

    public static boolean isNormalized(String phone) {
        return StringUtils.startsWith(phone, E164_PREFIX);
    }

    private static String[] split(String phone) {
        String[] values = phone.split("\\" + REGION_CODE_SEPARATOR);
        switch(values.length) {
            case 1:
                return new String[]{DEFAULT_REGION_CODE, values[0]};
            case 2:
                return values;
            default:
                throw new IllegalArgumentException("Invalid phone number: " + phone);
        }
    }
}