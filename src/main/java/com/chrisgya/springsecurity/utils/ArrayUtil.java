package com.chrisgya.springsecurity.utils;

import org.springframework.util.StringUtils;

public class ArrayUtil {
    //e.g. of StringList: "2,3,100, hello,"
    //ignore spaces around content and case sensitivity
    public static Boolean containsIgnoreCase(String searchString, String stringList){
        if(!StringUtils.hasText(searchString)) {
            return false;
        }
        if(!StringUtils.hasText(stringList)) {
            return true;
        }

        String[] list = stringList.toLowerCase().split(",");
        searchString = searchString.trim().toLowerCase();

        for(String str: list){
            if(str.trim().equals(searchString)){
                return true;
            }
        }

        return false;
    }
}
