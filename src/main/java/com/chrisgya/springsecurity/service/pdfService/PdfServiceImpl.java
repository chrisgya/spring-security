package com.chrisgya.springsecurity.service.pdfService;

import com.chrisgya.springsecurity.entity.User;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
@Service
public class PdfServiceImpl implements PdfService {

    private static final String PDF_RESOURCES = "/templates/mail/";
    private final SpringTemplateEngine templateEngine;


    @Override
    public ByteArrayInputStream userReport(User user, String tableTile, String[] tableColumns){
        var document = new Document();
        var out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            //add text to PDF file
            Font font = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
            var para = new Paragraph(tableTile, font);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(80);

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            Stream.of(tableColumns)
                    .forEach(headerTitle -> {
                        PdfPCell hCell;
                        //table header
                        hCell = new PdfPCell(new Phrase(headerTitle, headFont));
                        hCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        hCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        hCell.setBorderWidth(2);
                        table.addCell(hCell);
                    });


            //table content data
            PdfPCell cell;
            cell = new PdfPCell(new Phrase(user.getFirstName()));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPaddingLeft(4);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(user.getLastName()));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPaddingLeft(4);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(user.getEmail()));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPaddingRight(4);
            table.addCell(cell);


            document.add(table);
            document.close();
        } catch (DocumentException e) {
            log.error("PDF CONVERTING ERROR: {0}", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public File generatePdfFromHtml(User user) throws IOException, com.lowagie.text.DocumentException {
        Context context = new Context();
        Map<String, Object> variables = new HashMap<>();
        variables.put("user", user);
        context.setVariables(variables);

        String html = templateEngine.process("user-template", context);

            return renderPdf(html);
    }

    private File renderPdf(String html) throws IOException, com.lowagie.text.DocumentException {
        File file = File.createTempFile("user", ".pdf");
        OutputStream outputStream = new FileOutputStream(file);
        ITextRenderer renderer = new ITextRenderer(20f * 4f / 3f, 20);
        renderer.setDocumentFromString(html, new ClassPathResource(PDF_RESOURCES).getURL().toExternalForm());
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
        file.deleteOnExit();
        return file;
    }

}
