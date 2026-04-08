package com.adouk.finacombackend.infrastructure.rest.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CountDto{

    private int total;
    private int pending;
    private int validated;
    private int rejected;
}

