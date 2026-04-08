package com.adouk.finacombackend.infrastructure.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientCountByTypeDto {

    private int physic;
    private int entreprise;
    private int association;
}

