package com.adouk.finacombackend.Application.services.Impl;

import com.adouk.finacombackend.Application.services.Interfaces.UserQueryService;
import com.adouk.finacombackend.domain.aggregates.User;
import com.adouk.finacombackend.infrastructure.repositories.UserRepository;
import com.adouk.finacombackend.infrastructure.rest.dto.UserDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public List<UserDto> findAllAgents() {
        return userRepository.findAllAgents().stream()
                .map(user -> {
                    UserDto userDto = modelMapper.map(user, UserDto.class);
                    return userDto;
                })
                .collect(Collectors.toList());
    }
}
