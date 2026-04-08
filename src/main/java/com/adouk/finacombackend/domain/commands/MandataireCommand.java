package com.adouk.finacombackend.domain.commands;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Data
public class MandataireCommand {

    private String nom;
    private String prenom;
    private String lienParente;
    private String telephone;
    private String email;

    private Map<String, MultipartFile> fichiersJustificatifs;
}
