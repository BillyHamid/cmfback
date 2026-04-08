package com.adouk.finacombackend.infrastructure.rest.dto;

import lombok.Data;

import java.util.List;

@Data
public class SmsRequest {
    private String from;
    private String text;
    private List<String> to;
    private String send_at; // optionnel, format "HH:mm ddMMyyyy"


}

