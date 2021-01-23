package com.chrisgya.springsecurity.service.fileStorage;


import com.chrisgya.springsecurity.model.StoreFileResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface FileStorageService {
    StoreFileResponse storeFile(MultipartFile file, Boolean isImage, Integer originalFileNameMaxLength);

    String getFileFullPath(String fileName, HttpServletRequest request);

    Resource loadFileAsResource(String fileName);

    String convertFileToBase64(MultipartFile file, Boolean isImage);

    String convertFileToBase64(String originalFilePath);

    Boolean deleteFile(String fileName);

    void fileUploadValidation(MultipartFile file, Boolean isImage);
}
