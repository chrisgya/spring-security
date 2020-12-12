package com.chrisgya.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayInputStream;
import java.io.File;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
   // private final EmailProperties emailProperties;
//
//
//    @Override
//    @Async
//    public void emailSender(String templateName, String subject, String mailTo, Map<String, Object> model) {
//        Mail mail = new Mail();
//        mail.setFrom(emailProperties.getDefaults().getSender());
//
//        mail.setMailTo(mailTo);
//
//        mail.setSubject(subject);
//        if (model != null && !model.isEmpty()) {
//            mail.setProps(model);
//        }
//
//        try {
//            sendEmail(mail, templateName);
//            System.out.println("********************** EMAIL SENT ********************************");
//        } catch (MessagingException e) {
//            System.out.println("********************** ERROR SENDING EMAIL ********************************");
//            e.printStackTrace();
//        }
//    }
//
//
//    private void sendEmail(Mail mail, String templateName) throws MessagingException {
//        MimeMessage message = emailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
//        //  helper.addAttachment("template-cover.png", new ClassPathResource("javabydeveloper-email.PNG"));
//        Context context = new Context();
//        context.setVariables(mail.getProps());
//
//        String html = templateEngine.process(templateName, context);
//        // helper.setTo(mail.getMailTo());
//        helper.setTo(InternetAddress.parse(mail.getMailTo()));
//        helper.setText(html, true);
//        helper.setSubject(mail.getSubject());
//        helper.setFrom(mail.getFrom());
//        emailSender.send(message);
//
//    }


    @Override
    public void sendMail(String toEmail, String subject, String message) {

        var mailMessage = new SimpleMailMessage();

        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailMessage.setFrom("chrisgya@example.com");

        emailSender.send(mailMessage);
    }


    @Override
    public void sendMailWithAttachment(String to, String subject, String body, String fileToAttach, String attachmentNameWithExtension)
    {
        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setFrom(new InternetAddress("admin@gmail.com"));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(body);

            FileSystemResource file = new FileSystemResource(new File(fileToAttach));
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.addAttachment(attachmentNameWithExtension, file);
        };

        try {
            emailSender.send(preparator);
        }
        catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void sendMailWithAttachment(String to, String subject, String body, ByteArrayInputStream bis, String attachmentNameWithExtension) {

        emailSender.send(new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                var mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mimeMessageHelper.setTo(to);
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(body, true);

                DataSource attachment = new ByteArrayDataSource(bis, "application/pdf");

                mimeMessageHelper.addAttachment(attachmentNameWithExtension, attachment);

            }
        });
    }

}
