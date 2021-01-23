package com.chrisgya.springsecurity.utils;

import com.chrisgya.springsecurity.exception.BadRequestException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.codec.binary.Base64.*;

public class Base64ConverterUtil {

    public static byte[] convertBase64StringToByteArray(String base64String) {
        return decodeBase64(base64String.getBytes());
    }

    public static byte[] convertByteArrayToBase64String(byte[] byteArray) {
        return encodeBase64(byteArray);
    }

    public static String encodeStringInBase64(String str) {
        byte[] bytes = encodeBase64(str.getBytes());
        return new String(bytes);
    }

    public static String convertFileToBase64(MultipartFile file, int sizeLimit, String allowedExtensions) {
        try {
            FileUtil.validateFileNameAndTypeAndSize(file, sizeLimit, allowedExtensions);
            return encodeBase64String(file.getBytes());
        } catch (IOException ex) {
            throw new BadRequestException("invalid file", ex);
        }
    }

    public static String encodeFileToBase64Binary(String fileName) {
        try {
            byte[] fileContent = FileUtils.readFileToByteArray(new File(fileName));
            return Base64.encodeBase64String(fileContent);
        } catch (IOException ex) {
            throw new BadRequestException("invalid file", ex);
        }
    }

    public static String encodeFileToBase64Binary(File file) {
        try {
            byte[] fileContent = FileUtils.readFileToByteArray(file);
            return Base64.encodeBase64String(fileContent);
        } catch (IOException ex) {
            throw new BadRequestException("invalid file", ex);
        }
    }

}
