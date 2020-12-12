package com.chrisgya.springsecurity.service;

import java.io.ByteArrayInputStream;

public interface EmailService {
    void sendMail(String toEmail, String subject, String message);

    void sendMailWithAttachment(String to, String subject, String body, String fileToAttach, String attachmentNameWithExtension);

    void sendMailWithAttachment(String to, String subject, String body, ByteArrayInputStream bis, String attachmentNameWithExtension);
}
