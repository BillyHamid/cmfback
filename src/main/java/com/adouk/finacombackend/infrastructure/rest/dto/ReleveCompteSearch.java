package com.adouk.finacombackend.infrastructure.rest.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ReleveCompteSearch {

    private String statut;
    private String compte;
    private int page;
    private int size;
}
