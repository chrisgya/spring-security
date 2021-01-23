package com.chrisgya.springsecurity.utils;

import com.chrisgya.springsecurity.exception.BadRequestException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import static com.chrisgya.springsecurity.utils.validations.ValidationMessage.*;

public class FileUtil {

    public static void validateFileNameAndTypeAndSize(MultipartFile file, int sizeLimit, String allowedExtensions) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename()); // Normalize file name
        String ext = FilenameUtils.getExtension(fileName);

        if(fileName.contains("..")) {  // Check if the file's name contains invalid characters
            throw new BadRequestException(String.format(INVALID_FILE_NAME, fileName));
        }

        if (!ArrayUtil.containsIgnoreCase(ext, allowedExtensions)) {
            throw new BadRequestException(String.format(INVALID_FILE_EXTENSION, fileName));
        }

        long fileSize = file.getSize() / 1024;
        if (fileSize > sizeLimit) {
            throw new BadRequestException(String.format(EXCEEDED_FILE_SIZE_LIMIT, fileName, fileSize));
        }
    }

    public static String getMimeType(String fileExtension){
        if(fileExtension == null || !StringUtils.hasText(fileExtension)){
            throw new BadRequestException("file extension cannot be empty");
        }
        switch (fileExtension.trim().toLowerCase()){
            case "pdf":
                return "application/pdf";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            default:
                throw new BadRequestException("Unknown file mime-type");
        }
    }
}
