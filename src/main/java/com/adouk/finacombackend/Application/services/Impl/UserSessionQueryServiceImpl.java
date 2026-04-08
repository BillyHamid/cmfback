package com.adouk.finacombackend.Application.services.Impl;

import com.adouk.finacombackend.Application.services.Interfaces.UserSessionQueryService;
import com.adouk.finacombackend.infrastructure.repositories.UserSessionRepository;
import com.adouk.finacombackend.infrastructure.rest.dto.UserDto;
import com.adouk.finacombackend.infrastructure.rest.dto.UserSessionDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserSessionQueryServiceImpl implements UserSessionQueryService {

    private final UserSessionRepository userSessionRepository;
    private final ModelMapper modelMapper;


    @Override
    public List<UserSessionDto> findAll() {
        return userSessionRepository.findAll().stream()
                .map(userSession -> {
                    UserSessionDto userSessionDto = modelMapper.map(userSession, UserSessionDto.class);
                    userSessionDto.setUserDto(modelMapper.map(userSession.getUser(), UserDto.class));
                    return userSessionDto;
                })
                .collect(Collectors.toList());
    }
}
