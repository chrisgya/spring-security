package com.chrisgya.springsecurity.service.fileStorage;

import com.chrisgya.springsecurity.config.properties.FileProperties;
import com.chrisgya.springsecurity.controller.FileController;
import com.chrisgya.springsecurity.exception.BadRequestException;
import com.chrisgya.springsecurity.exception.NotFoundException;
import com.chrisgya.springsecurity.model.StoreFileResponse;
import com.chrisgya.springsecurity.utils.Base64ConverterUtil;
import com.chrisgya.springsecurity.utils.FileUtil;
import com.chrisgya.springsecurity.utils.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static com.chrisgya.springsecurity.utils.validations.ValidationMessage.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final FileProperties fileProperties;
    private Path fileStorageLocation;

    @PostConstruct
    public void init() {
        this.fileStorageLocation = Paths.get(fileProperties.getDirectory()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new BadRequestException(FILE_DIRECTORY_CREATION_ERROR, ex);
        }
    }

    @Override
    public StoreFileResponse storeFile(MultipartFile file, Boolean isImage, Integer originalFileNameMaxLength) {
        fileUploadValidation(file, isImage);
        var originalFile = StringUtils.cleanPath(file.getOriginalFilename()).split("\\."); // Normalize file name
        var originalFileName = originalFileNameMaxLength == null? originalFile[0] : originalFile[0].substring(0,Math.min(originalFile[0].length(), originalFileNameMaxLength));
        var extension = originalFile[1];

        try {
            String filePath = RandomUtil.randomAlphaNumeric(20) + "." + extension; //rename file before saving
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(filePath);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return new StoreFileResponse(originalFileName, extension, filePath);
        } catch (IOException ex) {
            throw new BadRequestException(String.format(FILE_STORAGE_FAILED, originalFileName), ex);
        }
    }

    @Override
    public void fileUploadValidation(MultipartFile file, Boolean isImage) {
        FileUtil.validateFileNameAndTypeAndSize(file, limitConversion(isImage), isImage ? fileProperties.getImageExtensions() : fileProperties.getFileExtensions());
    }


    @Override
    public String getFileFullPath(String fileName, HttpServletRequest request) {
        String fileUrl = MvcUriComponentsBuilder
                .fromMethodName(FileController.class, "serveFile", fileName, request)
                .build()
                .toString();

        return fileUrl;
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new NotFoundException(String.format(NOT_FOUND, fileName));
            }
        } catch (MalformedURLException e) {
            throw new BadRequestException(e.getMessage(), e);
        }
    }

    @Override
    public Boolean deleteFile(String fileName) {
        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            FileUtils.touch(new File(String.valueOf(targetLocation.toAbsolutePath())));
            File fileToDelete = FileUtils.getFile(String.valueOf(targetLocation.toAbsolutePath()));
            return FileUtils.deleteQuietly(fileToDelete);
        } catch (IOException e) {
            log.error(String.format(FILE_DELETION_FAILED, fileName), e);
        }
        return false;
    }


    @Override
    public String convertFileToBase64(MultipartFile file, Boolean isImage) {
        return Base64ConverterUtil.convertFileToBase64(file, limitConversion(isImage), isImage ? fileProperties.getImageExtensions() : fileProperties.getFileExtensions());
    }

    @Override
    public String convertFileToBase64(String originalFilePath) {
        String base64String = "";
        try {
            String[] filePath = originalFilePath.split("\\/");
            var fileName = filePath[filePath.length - 1];
            var file = loadFileAsResource(fileName).getFile();
            base64String = Base64ConverterUtil.encodeFileToBase64Binary(file);
        } catch (IOException e) {
            log.error("converting filePath to base64 string", e);
        }
        return base64String;
    }

    private int limitConversion(boolean isImage) {
        int limit = 204;
        try {
            String size = isImage ? fileProperties.getMaxImageSize().toString(): "1024";
            limit = Integer.parseInt(size);
        } catch (NumberFormatException exception) {
        }
        return limit;
    }

}
