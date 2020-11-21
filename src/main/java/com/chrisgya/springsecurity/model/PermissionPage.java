package com.chrisgya.springsecurity.model;

import lombok.Data;

@Data
public class PermissionPage extends BasePage{
    private String sortBy = "name";
}
