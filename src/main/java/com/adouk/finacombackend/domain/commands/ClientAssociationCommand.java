package com.adouk.finacombackend.domain.commands;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Getter
@Setter
public class ClientAssociationCommand extends ClientCommand {

    private String nomOrganisation;
    private String typeOrganisation;
    private String domaineActivite;

    private String nomRepresentant;
    private String prenomRepresentant;
    private String sexeRepresentant;
    private String telRepresentant;
    private String emailRepresentant;
    private String adresseRepresentant;

    private Map<String, MultipartFile> fichiersJustificatifs;
}
