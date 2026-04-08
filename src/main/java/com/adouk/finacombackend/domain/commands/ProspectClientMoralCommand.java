package com.adouk.finacombackend.domain.commands;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Setter
@Getter
public class ProspectClientMoralCommand extends ProspectClientCommand {

    private String raisonSociale;
    private String rccm;
    private String numIfu;
    private String formeJuridique;
    private String domaineActivite;

    private String nomResponsable;
    private String prenomResponsable;
    private String sexeResponsable;
    private String telResponsable;
    private String emailResponsable;
    private String adresseResponsable;

    private Map<String, MultipartFile> fichiersJustificatifs;

}
