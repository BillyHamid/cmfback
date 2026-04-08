package com.adouk.finacombackend.domain.commands;

import lombok.Data;

@Data
public class ProspectClientCommand {

    protected String adresse;
    protected String email;
    protected String telephone1;
    protected String telephone2;
    protected String bp;
}
