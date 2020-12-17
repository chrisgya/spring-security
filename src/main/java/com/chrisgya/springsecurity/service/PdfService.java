package com.chrisgya.springsecurity.service;

import com.chrisgya.springsecurity.entity.User;
import com.lowagie.text.DocumentException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public interface PdfService {
    ByteArrayInputStream userReport(User user, String tableTile, String[] tableColumns);

    File generatePdfFromHtml(User user) throws IOException, DocumentException;
}
