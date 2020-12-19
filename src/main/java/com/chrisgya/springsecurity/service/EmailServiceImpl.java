package com.chrisgya.springsecurity.service;

import com.chrisgya.springsecurity.model.Mail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;


    @Override
    @Async
    public void sender(String templateName, String subject, String mailTo, Map<String, Object> thymeLeafProps, Map<String, String> fileAttachments, Map<String, ByteArrayInputStream> bisAttachments, MediaType mediaType) {
        Mail mail = Mail.builder()
                .from("chrisgya@gmail.com")
                .mailTo(mailTo)
                .subject(subject)
                .fileAttachments(fileAttachments)
                .bisAttachments(bisAttachments)
                .thymeLeafProps(thymeLeafProps)
                .mediaType(mediaType)
                .build();

        try {
            sendEmail(mail, templateName);
            System.out.println("********************** EMAIL SENT ********************************");
        } catch (MessagingException e) {
            System.out.println("********************** ERROR SENDING EMAIL ********************************");
            e.printStackTrace();
        }
    }


    private void sendEmail(Mail mail, String templateName) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        if (mail.getFileAttachments() != null) {
            mail.getFileAttachments().forEach((name, filePath) -> {
                try {
                    //  helper.addAttachment("template-cover.png", new ClassPathResource("javabydeveloper-email.PNG"));

                    FileSystemResource file = new FileSystemResource(new File(filePath));
                    helper.addAttachment(name, file);
                } catch (MessagingException e) {
                    log.error(e.getMessage());
                }
            });
        }


        if (mail.getBisAttachments() != null) {
            mail.getBisAttachments().forEach((name, bis) -> {
                try {
                    DataSource attachment = new ByteArrayDataSource(bis, mail.getMediaType().toString());

                    helper.addAttachment(name, attachment);

                } catch (MessagingException | IOException e) {
                    log.error(e.getMessage());
                }
            });
        }

        Context context = new Context();
        if (mail.getThymeLeafProps() != null) {
            context.setVariables(mail.getThymeLeafProps());
        }

        String html = templateEngine.process(templateName, context);
        // helper.setTo(mail.getMailTo());
        helper.setTo(InternetAddress.parse(mail.getMailTo()));
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom());
        emailSender.send(message);

    }

    @Async
    @Override
    public void sender(String to, String subject, String body, ByteArrayInputStream bis, MediaType mediaType, String attachmentNameWithExtension) {

        emailSender.send(mimeMessage -> {
            var mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body, true);

            DataSource attachment = new ByteArrayDataSource(bis, mediaType.toString());

            mimeMessageHelper.addAttachment(attachmentNameWithExtension, attachment);

        });
    }


//    @Override
//    public void sendMail(String toEmail, String subject, String message) {
//
//        var mailMessage = new SimpleMailMessage();
//
//        mailMessage.setTo(toEmail);
//        mailMessage.setSubject(subject);
//        mailMessage.setText(message);
//
//        mailMessage.setFrom("chrisgya@example.com");
//
//        emailSender.send(mailMessage);
//    }


//    @Override
//    public void sendMailWithAttachment(String to, String subject, String body, String fileToAttach, String attachmentNameWithExtension)
//    {
//        MimeMessagePreparator preparator = mimeMessage -> {
//            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
//            mimeMessage.setFrom(new InternetAddress("admin@gmail.com"));
//            mimeMessage.setSubject(subject);
//            mimeMessage.setText(body);
//
//            FileSystemResource file = new FileSystemResource(new File(fileToAttach));
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
//            helper.addAttachment(attachmentNameWithExtension, file);
//        };
//
//        try {
//            emailSender.send(preparator);
//        }
//        catch (MailException ex) {
//            // simply log it and go on...
//            System.err.println(ex.getMessage());
//        }
//    }


}
