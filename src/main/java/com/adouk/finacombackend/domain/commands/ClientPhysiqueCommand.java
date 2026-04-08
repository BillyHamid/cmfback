package com.adouk.finacombackend.domain.commands;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
public class ClientPhysiqueCommand extends ClientCommand {

    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String lieuNaissance;
    private String profession;
    private String employeur;
    private String adresseEmployeur;
    private String numeroCNIB;

    private Map<String, MultipartFile> fichiersJustificatifs;

}
