package com.adouk.finacombackend.Application.services.Interfaces;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {

    List<String> storeToS3(String clientNumber, String restaurant, List<MultipartFile> files);
    String storeToS3(String clientNumber, String restaurant, MultipartFile file);
    String generatePresignedUrl(String key);

    /** Lecture du fichier (S3 ou disque selon configuration). */
    Resource getObjectAsResource(String key);
}
