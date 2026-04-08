package com.adouk.finacombackend.Application.services.Impl;

import com.adouk.finacombackend.Application.services.Interfaces.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.storage.mode", havingValue = "s3")
public class FileStorageServiceImpl implements FileStorageService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public String storeToS3(String clientNumber, String fileName, MultipartFile file) {
        String key = clientNumber + "/" + fileName + getExtension(file.getOriginalFilename());
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(
                    request,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            return key; // Retourne juste la clé (pas le lien public direct)
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l’upload de fichier : " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public List<String> storeToS3(String clientNumber, String fileName, List<MultipartFile> files) {
        List<String> keys = new ArrayList<>();

        for (MultipartFile file : files) {
            String key = clientNumber + "/" + fileName + getExtension(file.getOriginalFilename());

            try {
                PutObjectRequest request = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(file.getContentType())
                        .build();

                s3Client.putObject(
                        request,
                        RequestBody.fromInputStream(file.getInputStream(), file.getSize())
                );

                keys.add(key);
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors de l’upload de fichier : " + file.getOriginalFilename(), e);
            }
        }

        return keys;
    }

    @Override
    public String generatePresignedUrl(String key) {
        try (S3Presigner presigner = S3Presigner.builder()
//                .region(Region.US_EAST_2)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build()) {

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .getObjectRequest(getObjectRequest)
                    .build();

            String url = presigner.presignGetObject(presignRequest).url().toString();
            System.out.println(url);
            return url;

        }
    }



    @Override
    public Resource getObjectAsResource(String key) {
        var object = s3Client.getObject(
                GetObjectRequest.builder().bucket(bucketName).key(key).build());
        return new InputStreamResource(object);
    }

    private String getExtension(String originalFilename) {
        if (originalFilename != null && originalFilename.contains(".")) {
            return originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return "";
    }
}
