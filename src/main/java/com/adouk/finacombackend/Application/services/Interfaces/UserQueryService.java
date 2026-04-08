package com.adouk.finacombackend.Application.services.Interfaces;

import com.adouk.finacombackend.domain.aggregates.User;
import com.adouk.finacombackend.infrastructure.rest.dto.UserDto;

import java.util.List;

public interface UserQueryService {

    User findByUsername(String username);
    List<UserDto> findAllAgents();

}
