package com.adouk.finacombackend.domain.commands;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ClientCommand {

    protected String adresse;
    protected String email;
    protected String telephone1;
    protected String telephone2;
    protected String bp;
    protected UUID agenceId;
    protected String idSib;
    protected boolean enableMobileAccess;
}
