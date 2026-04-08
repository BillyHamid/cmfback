package com.adouk.finacombackend.infrastructure.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDto {

    private String token;
    private Object userDetails;

}
