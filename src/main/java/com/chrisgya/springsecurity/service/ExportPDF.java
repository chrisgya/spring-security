package com.chrisgya.springsecurity.service;

import com.chrisgya.springsecurity.entity.User;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.stream.Stream;

@Slf4j
public class ExportPDF {

    public static ByteArrayInputStream userReport(User user){
        var document = new Document();
        var out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            //add text to PDF file
            Font font = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
            var para = new Paragraph("User Table", font);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(80);

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            Stream.of("First Name", "Last Name", "Email")
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
}
