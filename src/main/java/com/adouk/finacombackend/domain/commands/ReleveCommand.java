package com.adouk.finacombackend.domain.commands;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class ReleveCommand {

    private UUID compteBancaireUuid;
    private Date intervalleDebut;
    private Date intervalleFin;
}
