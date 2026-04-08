package com.adouk.finacombackend.infrastructure.rest.controllers;

import com.adouk.finacombackend.Application.services.Interfaces.FileStorageService;
import com.adouk.finacombackend.Application.services.Interfaces.PieceJustificativeService;
import com.adouk.finacombackend.infrastructure.rest.dto.PieceJustificativeDto;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/piece-justificatives")
@AllArgsConstructor
public class PieceJustificativeController {

     private final PieceJustificativeService pieceJustificativeService;

        private final FileStorageService fileStorageService;

     @GetMapping("/client/{clientId}")
     public ResponseEntity<List<PieceJustificativeDto>> getByClient(@PathVariable UUID clientId) {
         List<PieceJustificativeDto> pieces = pieceJustificativeService.getByClient(clientId);
         return ResponseEntity.ok(pieces);
     }

    @GetMapping("/get-presigned-url")
    public ResponseEntity<Map<String, String>> getPresignedUrl(@RequestParam String key) {
        String url = fileStorageService.generatePresignedUrl(key);
        return ResponseEntity.ok(Map.of("url", url));
    }


    @GetMapping("/files")
    public ResponseEntity<Resource> getFile(@RequestParam String key) {
        Resource resource = fileStorageService.getObjectAsResource(key);

        String contentType = URLConnection.guessContentTypeFromName(key);
        if (contentType == null || contentType.isEmpty()) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        String fileName = key.contains("/") ? key.substring(key.lastIndexOf("/") + 1) : key;

        boolean inlinePreview = contentType.startsWith("image/") || contentType.equals("application/pdf");

        return ResponseEntity.ok()
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, (inlinePreview ? "inline" : "attachment") + "; filename=\"" + fileName + "\"")
                .body(resource);

    }




}
