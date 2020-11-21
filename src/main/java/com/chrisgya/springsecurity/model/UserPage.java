package com.chrisgya.springsecurity.model;

import lombok.Data;

@Data
public class UserPage extends BasePage{
    private String sortBy = "first_name";
}
