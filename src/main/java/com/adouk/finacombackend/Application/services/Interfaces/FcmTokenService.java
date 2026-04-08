package com.adouk.finacombackend.Application.services.Interfaces;


import com.adouk.finacombackend.domain.commands.FcmTokenRequest;

public interface FcmTokenService {
    void saveOrUpdateToken(FcmTokenRequest request);
    void removeToken(String token);
}
