package com.chrisgya.springsecurity.service;

import org.springframework.scheduling.annotation.Async;

import java.io.ByteArrayInputStream;
import java.util.Map;

public interface EmailService {

    @Async
    void sender(String templateName, String subject, String mailTo, Map<String, Object> thymeLeafProps, Map<String, String> fileAttachments, Map<String, ByteArrayInputStream> bisAttachments);

    @Async
    void sender(String to, String subject, String body, ByteArrayInputStream bis, String attachmentNameWithExtension);
}
