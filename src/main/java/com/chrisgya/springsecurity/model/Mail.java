package com.chrisgya.springsecurity.model;

import lombok.Builder;
import lombok.Data;

import java.io.ByteArrayInputStream;
import java.util.Map;

@Builder
@Data
public class Mail{
    private String from;
    private String mailTo;
    private String subject;
    private Map<String, String> fileAttachments;
    private Map<String, ByteArrayInputStream> bisAttachments;
    private Map<String, Object> thymeLeafProps;
}
