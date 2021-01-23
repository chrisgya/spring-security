package com.chrisgya.springsecurity.service.excelService;

import com.chrisgya.springsecurity.entity.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ExcelFileExporter {

    public static ByteArrayInputStream contactListToExcelFile(String[] columns, List<User> users) {
        try(Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("Users");

            Row row = sheet.createRow(0);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Creating header
            Cell cell;
            for(int i=0; i < columns.length; i++){
                cell = row.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);

                // Making size of column auto resize to fit with data
                sheet.autoSizeColumn(i);
           }

            // Creating data rows for each customer
            for(int i = 0; i < users.size(); i++) {
                Row dataRow = sheet.createRow(i + 1);
                dataRow.createCell(0).setCellValue(users.get(i).getFirstName());
                dataRow.createCell(1).setCellValue(users.get(i).getLastName());
                dataRow.createCell(2).setCellValue(users.get(i).getUsername());
                dataRow.createCell(3).setCellValue(users.get(i).getEmail());
            }


            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void downloadCsv(PrintWriter writer, List<User> users) {
        writer.write("User Name, First Name, Last Name \n");
        for (User user : users) {
            writer.write(user.getUsername() + "," + user.getFirstName() + "," + user.getLastName() + "\n");
        }
    }

}
