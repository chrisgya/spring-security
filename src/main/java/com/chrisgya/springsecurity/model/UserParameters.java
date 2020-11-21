package com.chrisgya.springsecurity.model;

import lombok.Data;

@Data
public class UserParameters {
    private String usersUsername;
    private String usersEmail;
    private String usersFirstName;
    private String usersLastName;
    private Boolean usersIsLocked;
    private Boolean usersIsEnabled;
    private Boolean usersIsConfirmed;
    private Boolean usersIsDeleted;
}