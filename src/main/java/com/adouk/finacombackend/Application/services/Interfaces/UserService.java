package com.adouk.finacombackend.Application.services.Interfaces;

import com.adouk.finacombackend.domain.aggregates.User;
import com.adouk.finacombackend.domain.commands.UserCommand;
import com.adouk.finacombackend.domain.commands.UserPasswordUpdateCommand;
import com.adouk.finacombackend.infrastructure.rest.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDto createAgent(UserCommand userCommand);
    void createClient(UserCommand userCommand);
    UserDto update(UserCommand userCommand, UUID id);
    UserDto updatePassword(UserPasswordUpdateCommand userPasswordUpdateCommand);
    UserDto reinitializePassword(UserPasswordUpdateCommand userPasswordUpdateCommand, Principal principal);
    UserDto blockUser(UUID userId);
    UserDto enableUser(UUID userId);
    List<String> getPrivileges(User user);
    List<GrantedAuthority> getGrantedAuthorities(List<String> privileges);
    void delete(UUID id);
}
