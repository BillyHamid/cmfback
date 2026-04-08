package com.adouk.finacombackend.Application.services.Impl;

import com.adouk.finacombackend.Application.configs.AuthenticationService;
import com.adouk.finacombackend.Application.services.Interfaces.FcmTokenService;
import com.adouk.finacombackend.domain.aggregates.FcmToken;
import com.adouk.finacombackend.domain.aggregates.User;
import com.adouk.finacombackend.domain.commands.FcmTokenRequest;
import com.adouk.finacombackend.infrastructure.repositories.ClientRepository;
import com.adouk.finacombackend.infrastructure.repositories.FcmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmTokenServiceImpl implements FcmTokenService {

    private final FcmTokenRepository repository;
    private final ClientRepository clientRepository;

    @Override
    public void saveOrUpdateToken(FcmTokenRequest request) {

        repository.findByToken(request.getToken()).ifPresentOrElse(
                existing -> {
                    existing.setBrand(request.getBrand());
                    existing.setModel(request.getModel());
                    existing.setOs(request.getOs());
                    existing.setOsVersion(request.getOsVersion());
                    existing.setUniqueId(request.getUniqueId());
                    existing.setClient(clientRepository.findById(request.getClientId()).orElseThrow());
                    repository.save(existing);
                },
                () -> {
                    FcmToken token = new FcmToken();
                    token.setToken(request.getToken());
                    token.setBrand(request.getBrand());
                    token.setModel(request.getModel());
                    token.setOs(request.getOs());
                    token.setOsVersion(request.getOsVersion());
                    token.setUniqueId(request.getUniqueId());
                    token.setClient(clientRepository.findById(request.getClientId()).orElseThrow());
                    repository.save(token);
                }
        );
    }

    @Override
    public void removeToken(String token) {
        repository.findByToken(token).ifPresent(repository::delete);
    }
}
