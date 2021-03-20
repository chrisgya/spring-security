package com.chrisgya.springsecurity.controller;

import com.chrisgya.springsecurity.entity.User;
import com.chrisgya.springsecurity.service.emailService.EmailService;
import com.chrisgya.springsecurity.service.excelService.ExcelFileExporter;
import com.chrisgya.springsecurity.service.pdfService.PdfService;
import com.chrisgya.springsecurity.service.userService.UserService;
import com.lowagie.text.DocumentException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.chrisgya.springsecurity.utils.CommonUtils.retrieveByteArrayInputStream;

@Tag(name = "Demo management By Administrator", description = "This is experimental services")
@SecurityRequirement(name = "api")
@RestController
@RequestMapping("api/v1/demos")
@RequiredArgsConstructor
public class DemoController {

    private final UserService userService;
    private final EmailService emailService;
    private final PdfService pdfService;

    //    hasRole('ROLE_') hasAnyRole('ROLE_') hasAuthority('permission') hasAnyAuthority('permission')

    @Operation(summary = "get faker users", description = "demonstrating how hasAnyRole Authorization works")
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINTRAINEE')")
    public List<FakeUser> getAllFakeUsers() {
        System.out.println("getAllFakeUsers");
        return Arrays.asList(new FakeUser(1, "john"),
                new FakeUser(2, "sam"),
                new FakeUser(3, "doe"));
    }


    // http://localhost:8080/security/api/v1/users/export-pdf/1
    @Operation(summary = "export user to pdf and email at the same time", description = "Demonstrating how to export user details to pdf and send it as email as well")
    @GetMapping(value = "/export-pdf/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> userReport(@PathVariable long id) {
        var user = userService.getUser(id);
        var bis = pdfService.userReport(user, "User Table", new String[]{"First Name", "Last Name", "Email"});

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report_" + user.getUsername() + ".pdf"); //inline or attachment

        sendEmail(user, bis, MediaType.APPLICATION_PDF, "users_report.pdf");
        sendEmailWithHtmlTemplate(user, bis, MediaType.APPLICATION_PDF, "users_report.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    // http://localhost:8080/security/api/v1/users/export-html-pdf/1
    @Operation(summary = "export user to pdf and email at the same time by first formatting the data in html", description = "Demonstrating how to convert data into html before export user details to pdf and send it as email as well")
    @GetMapping("/export-html-pdf/{id}")
    public void generatePdfFromHtml(@PathVariable long id, HttpServletResponse response) {
        var user = userService.getUser(id);

        try {
            var file = pdfService.generatePdfFromHtml(user);
            Path filePath = Paths.get(file.getAbsolutePath());
            if (Files.exists(filePath)) {
                //send email
                var bis = retrieveByteArrayInputStream(file);
                sendEmail(user, bis, MediaType.APPLICATION_PDF, "users_report.pdf");
                sendEmailWithHtmlTemplate(user, bis, MediaType.APPLICATION_PDF, "users_report.pdf");

                //export pdf
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=" + filePath.getFileName());
                Files.copy(filePath, response.getOutputStream());
                response.getOutputStream().flush();
            }
        } catch (DocumentException | IOException ex) {
            ex.printStackTrace();
        }
    }

    // http://localhost:8080/security/api/v1/users/export-excel
    @Operation(summary = "export user details to excel", description = "demonstrating how to export user details to excel")
    @GetMapping("export-excel")
    public void downloadExcel(HttpServletResponse response) throws IOException {

        var columns = new String[]{"First Name", "Last Name", "User Name", "Email"};
        var users = userService.getUsers();
        var stream = ExcelFileExporter.contactListToExcelFile(columns, users);
        // sendEmailWithHtmlTemplate(users.get(0), stream, MediaType.APPLICATION_OCTET_STREAM, "users_report.xlsx");
        sendEmail(users.get(0), stream, MediaType.APPLICATION_OCTET_STREAM, "users_report.xlsx");

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=users.xlsx");
        IOUtils.copy(stream, response.getOutputStream());
    }

    // http://localhost:8080/security/api/v1/users/export-csv.csv
    @Operation(summary = "export user details to csv", description = "demonstrating how to export user details to csv")
    @GetMapping("export-csv.csv")
    public void downloadCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=user.csv");
        var users = userService.getUsers();
        ExcelFileExporter.downloadCsv(response.getWriter(), users);
    }

    private void sendEmail(User user, ByteArrayInputStream bis, MediaType mediaType, String attachmentName) {
        var subject = "sending email with attachment example";
        var body = "<html><body> <p><strong>hello</strong>, <br /><br /> This is just a testing message body.</p> <br /><br /> Thank you.</body></html>";

        emailService.sender(user.getEmail(), subject, body, bis, mediaType, attachmentName);
    }

    private void sendEmailWithHtmlTemplate(User user, ByteArrayInputStream bis, MediaType mediaType, String attachmentName) {
        var subject = "sending email with html template & attachment example";
        var body = "<html><body> <p><strong>hello</strong>, <br /><br /> This is just a testing message body.</p> <br /><br /> Thank you.</body></html>";

        Map<String, Object> thymeLeafProps = new HashMap<>();
        thymeLeafProps.put("name", user.getFirstName());
        thymeLeafProps.put("body", body);

        Map<String, ByteArrayInputStream> bisAttachments = new HashMap<>();
        bisAttachments.put(attachmentName, bis);
        emailService.sender("base-email-template", subject, user.getEmail(), thymeLeafProps, null, bisAttachments, mediaType);

    }


}
