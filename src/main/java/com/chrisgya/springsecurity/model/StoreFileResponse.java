package com.chrisgya.springsecurity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StoreFileResponse {
    private String originalName;
    private String extension;
    private String path;
}
