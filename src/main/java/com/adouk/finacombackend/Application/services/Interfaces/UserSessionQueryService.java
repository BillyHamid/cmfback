package com.adouk.finacombackend.Application.services.Interfaces;

import com.adouk.finacombackend.infrastructure.rest.dto.UserSessionDto;

import java.util.List;

public interface UserSessionQueryService {

    List<UserSessionDto> findAll();

}
