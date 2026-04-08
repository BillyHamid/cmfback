package com.adouk.finacombackend.Application.services.Interfaces;

import com.adouk.finacombackend.domain.commands.AuthCommand;
import com.adouk.finacombackend.infrastructure.rest.dto.AuthResponseDto;

import java.security.Principal;

public interface AuthService {

    AuthResponseDto login(AuthCommand authCommand, String ipAdress);

    String logout(Principal principal);


}
