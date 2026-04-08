package com.adouk.finacombackend.infrastructure.rest.dto;


import com.adouk.finacombackend.domain.commands.FcmTokenRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationRequest {

    private String username;
    private String password;
    private FcmTokenRequest fcmTokenRequest;
}
