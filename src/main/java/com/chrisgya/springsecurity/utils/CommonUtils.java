package com.chrisgya.springsecurity.utils;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class CommonUtils {

    public static ByteArrayInputStream retrieveByteArrayInputStream(File file) throws IOException {
        return new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
    }

}
