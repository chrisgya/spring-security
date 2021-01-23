package com.chrisgya.springsecurity.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserParameters {
    private String usersUsername;
    private String usersEmail;
    private String usersFirstName;
    private String usersLastName;
    private Boolean usersLocked;
    private Boolean usersEnabled;
    private Boolean usersConfirmed;
}