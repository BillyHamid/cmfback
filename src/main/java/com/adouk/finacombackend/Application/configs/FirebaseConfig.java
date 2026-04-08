package com.adouk.finacombackend.Application.configs;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class FirebaseConfig {

    /**
     * Chemin absolu vers le JSON du compte de service (hors dépôt Git).
     * Exemple Windows : C:/Users/xxx/.secrets/finacom-firebase-adminsdk.json
     * Laisser vide pour ne pas initialiser Firebase (notifications désactivées).
     */
    @Value("${firebase.credentials.path:}")
    private String credentialsPath;

    @PostConstruct
    public void initialize() {
        try (InputStream serviceAccount = openCredentialsStream()) {
            if (serviceAccount == null) {
                System.err.println(
                    "⚠️ Firebase non configuré : définissez firebase.credentials.path vers le JSON "
                        + "du compte de service (fichier hors dépôt), ou les notifications push ne fonctionneront pas."
                );
                return;
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase initialisé avec succès");
            }

        } catch (Exception e) {
            System.err.println("❌ Erreur d'initialisation Firebase : " + e.getMessage());
        }
    }

    private InputStream openCredentialsStream() throws IOException {
        if (credentialsPath != null && !credentialsPath.isBlank()) {
            Path p = Path.of(credentialsPath.trim());
            if (Files.isRegularFile(p) && Files.isReadable(p)) {
                return Files.newInputStream(p);
            }
            System.err.println("⚠️ firebase.credentials.path introuvable ou illisible : " + p.toAbsolutePath());
        }
        return null;
    }
}
