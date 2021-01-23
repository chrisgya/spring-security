package com.chrisgya.springsecurity.controller;

import com.chrisgya.springsecurity.service.fileStorage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by christian.gyaban @ 15/03/2020
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/files")
public class FileController {
    private final FileStorageService fileStorageService;

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename, HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResource(filename);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
