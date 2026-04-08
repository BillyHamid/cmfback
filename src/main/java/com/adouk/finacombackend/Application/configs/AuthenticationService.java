package com.adouk.finacombackend.Application.configs;


import com.adouk.finacombackend.Application.services.Impl.EmailService;
import com.adouk.finacombackend.Application.services.Interfaces.FcmTokenService;
import com.adouk.finacombackend.domain.aggregates.Permission;
import com.adouk.finacombackend.domain.aggregates.User;
import com.adouk.finacombackend.domain.beans.PasswordUtils;
import com.adouk.finacombackend.infrastructure.repositories.RolePermissionRepository;
import com.adouk.finacombackend.infrastructure.repositories.UserRepository;
import com.adouk.finacombackend.infrastructure.repositories.UserSessionRepository;
import com.adouk.finacombackend.infrastructure.rest.dto.AuthWith2FAResponse;
import com.adouk.finacombackend.infrastructure.rest.dto.AuthenticationRequest;
import com.adouk.finacombackend.infrastructure.rest.dto.AuthenticationResponse;
import com.adouk.finacombackend.infrastructure.rest.dto.UserDto;
import com.adouk.finacombackend.infrastructure.rest.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;
    private final EmailService emailService;

    private final RolePermissionRepository rolePermissionRepository;
    private final FcmTokenService fcmTokenService;

    public AuthWith2FAResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername()).orElse(null);
        assert user != null;
        if (user.isEnabled2FA()) {
            AuthWith2FAResponse response = new AuthWith2FAResponse();
            response.setRequires2fa(true);
            return response;
        }

        return null;
    }

    public AuthenticationResponse authentificateClient(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername()).orElse(null);
        if(user != null && user.getAgent() != null){
            throw new BusinessException("Vous n'êtes pas autorisé à vous authentifier !");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var jwt = jwtService.generateToken(user);
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token(jwt)
                .user(modelMapper.map(user, UserDto.class))
                .build();

        assert user != null;
        user.setLastAuthDate(new Date());
        userRepository.save(user);

        // Save or update FCM token if present
        if (request.getFcmTokenRequest() != null) {
            request.getFcmTokenRequest().setClientId(user.getClient().getId());
            fcmTokenService.saveOrUpdateToken(request.getFcmTokenRequest());
        }

        return response;
    }

    public AuthenticationResponse authentificateAdmin(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername()).orElse(null);
        if(user != null && user.getClient() != null){
            throw new BusinessException("Vous n'êtes pas autorisé à vous authentifier !");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var jwt = jwtService.generateToken(user);
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token(jwt)
                .user(modelMapper.map(user, UserDto.class))
                .build();

        user.setLastAuthDate(new Date());
        userRepository.save(user);
        return response;
    }



    public User getConectedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return modelMapper.map(auth.getPrincipal(), User.class);
    }


    public Map<String, String> resetPass(String email) {
        User user = userRepository.findByUsername(email).orElseThrow(() -> new BusinessException("Utilisateur non trouvé"));
        if (user.getClient() != null) {
            throw new BusinessException("Vous n'êtes pas autorisé à réinitialiser le mot de passe !");
        }
        String password = PasswordUtils.generatePassword(8);
        user.setPassword(encoder.encode(password));
        userRepository.save(user);
        try {
            emailService.sendResetPasswordEmail(user.getAgent().getFirstname(),user.getAgent().getEmail(), user.getUsername(), password);
        } catch (Exception e) {
            log.error("Error sending email to user: {} with password: {}", user.getUsername(), password);
        }
        return Map.of("message", "Mot de passe réinitialisé avec succès");
    }



}
