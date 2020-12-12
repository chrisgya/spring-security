package com.chrisgya.springsecurity.controller;

import com.chrisgya.springsecurity.entity.User;
import com.chrisgya.springsecurity.service.EmailService;
import com.chrisgya.springsecurity.service.ExportPDF;
import com.chrisgya.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("api/v1/users")
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final EmailService emailService;


    @GetMapping(value = "/export-pdf/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> userReport(@PathVariable long id) {
        var user = userService.getUser(id);
        var bis = ExportPDF.userReport(user);

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report_" + user.getUsername()+ ".pdf"); //inline or attachment
    
        sendEmail(user, bis);

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    private void sendEmail(User user, ByteArrayInputStream bis) {
        var subject = "sending email with attachment example";
        var body = "<html><body> <p><strong>hello</strong>, <br /><br /> This is just a testing message body.</p> <br /><br /> Thank you.</body></html>";
        emailService.sendMailWithAttachment(user.getEmail(), subject, body, bis, user.getUsername() + " report.pdf");
    }


}
