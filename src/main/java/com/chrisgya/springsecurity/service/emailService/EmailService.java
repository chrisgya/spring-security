package com.chrisgya.springsecurity.service.emailService;

import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;

import java.io.ByteArrayInputStream;
import java.util.Map;

public interface EmailService {

    @Async
    void sender(String templateName, String subject, String mailTo, Map<String, Object> thymeLeafProps, Map<String, String> fileAttachments, Map<String, ByteArrayInputStream> bisAttachments, MediaType mediaType);

    @Async
    void sender(String to, String subject, String body, ByteArrayInputStream bis, MediaType mediaType, String attachmentNameWithExtension);
}
