package com.adouk.finacombackend.Application.services.Impl;

import com.adouk.finacombackend.Application.services.Interfaces.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Stockage sur disque (pas de MinIO/S3). Activé lorsque {@code app.storage.mode=local}
 * (profil {@code demo} par défaut).
 */
@Service
@ConditionalOnProperty(name = "app.storage.mode", havingValue = "local")
public class LocalFileStorageServiceImpl implements FileStorageService {

    @Value("${app.storage.local.directory:${user.home}/.finacom/uploads}")
    private String baseDirectory;

    @Value("${app.public.api-base-url:http://localhost:8080}")
    private String publicApiBaseUrl;

    private Path basePath() {
        return Paths.get(baseDirectory).toAbsolutePath().normalize();
    }

    @Override
    public String storeToS3(String clientNumber, String fileName, MultipartFile file) {
        String key = clientNumber + "/" + fileName + getExtension(file.getOriginalFilename());
        try {
            Path target = basePath().resolve(key).normalize();
            if (!target.startsWith(basePath())) {
                throw new SecurityException("Chemin de fichier invalide");
            }
            Files.createDirectories(target.getParent());
            try (var in = file.getInputStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }
            return key;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l’upload de fichier : " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public List<String> storeToS3(String clientNumber, String fileName, List<MultipartFile> files) {
        List<String> keys = new ArrayList<>();
        for (MultipartFile file : files) {
            keys.add(storeToS3(clientNumber, fileName, file));
        }
        return keys;
    }

    @Override
    public String generatePresignedUrl(String key) {
        return publicApiBaseUrl.replaceAll("/$", "")
                + "/api/v1/admin/piece-justificatives/files?key="
                + URLEncoder.encode(key, StandardCharsets.UTF_8);
    }

    @Override
    public Resource getObjectAsResource(String key) {
        Path p = basePath().resolve(key).normalize();
        if (!p.startsWith(basePath())) {
            throw new SecurityException("Clé de fichier invalide");
        }
        if (!Files.exists(p) || !Files.isRegularFile(p)) {
            throw new IllegalArgumentException("Fichier introuvable : " + key);
        }
        return new FileSystemResource(p);
    }

    private String getExtension(String originalFilename) {
        if (originalFilename != null && originalFilename.contains(".")) {
            return originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return "";
    }
}
