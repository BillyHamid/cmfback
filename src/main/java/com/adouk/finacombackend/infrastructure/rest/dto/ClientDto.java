package com.adouk.finacombackend.infrastructure.rest.dto;

import com.adouk.finacombackend.domain.aggregates.Agence;
import com.adouk.finacombackend.domain.aggregates.Agent;
import com.adouk.finacombackend.domain.aggregates.ClientStatus;
import com.adouk.finacombackend.domain.aggregates.User;
import com.adouk.finacombackend.domain.valueObjects.Audit;
import lombok.Data;

import java.util.UUID;

@Data
public class ClientDto extends Audit {

    protected UUID id;
    protected String adresse;
    protected String email;
    protected String telephone1;
    protected String telephone2;
    protected String bp;
    protected String idSib;
    protected String mentionParticuliere;
    protected ClientStatus kycStatus;
    protected Agent agentValidateur;
    protected Boolean kycValidated = false;
    protected Agence agenceRattachee;
    protected boolean hasMobileAccess;



}
