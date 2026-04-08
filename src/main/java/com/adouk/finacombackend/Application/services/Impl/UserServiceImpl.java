package com.adouk.finacombackend.Application.services.Impl;

import com.adouk.finacombackend.Application.services.Interfaces.UserService;
import com.adouk.finacombackend.domain.aggregates.Agent;
import com.adouk.finacombackend.domain.aggregates.Permission;
import com.adouk.finacombackend.domain.aggregates.User;
import com.adouk.finacombackend.domain.beans.PasswordUtils;
import com.adouk.finacombackend.domain.commands.UserCommand;
import com.adouk.finacombackend.domain.commands.UserPasswordUpdateCommand;
import com.adouk.finacombackend.infrastructure.repositories.*;
import com.adouk.finacombackend.infrastructure.rest.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;
    private final AgenceRepository agenceRepository;
    private final AgentRepository agentRepository;
    private final RoleRepository roleRepository;
    private final ClientRepository clientRepository;
    private final EmailService emailService;

    @Value("${app.demo.admin.password:}")
    private String demoAdminPassword;

    @Value("${app.demo.commercial.password:}")
    private String demoCommercialPassword;

    public UserServiceImpl(UserRepository userRepository, RolePermissionRepository rolePermissionRepository,
                          PasswordEncoder encoder, ModelMapper modelMapper, AgenceRepository agenceRepository,
                          AgentRepository agentRepository, RoleRepository roleRepository,
                          ClientRepository clientRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.encoder = encoder;
        this.modelMapper = modelMapper;
        this.agenceRepository = agenceRepository;
        this.agentRepository = agentRepository;
        this.roleRepository = roleRepository;
        this.clientRepository = clientRepository;
        this.emailService = emailService;
    }

    @Override
    public UserDto createAgent(UserCommand userCommand) {
        Agent agent = new Agent();
        agent.setFirstname(userCommand.getFirstname());
        agent.setLastname(userCommand.getLastname());
        agent.setPhone(userCommand.getPhone());
        agent.setEmail(userCommand.getEmail());
        if (userCommand.getAgenceId() != null) {
            agent.setAgence(agenceRepository.findById(userCommand.getAgenceId()).orElseThrow());
        }
        agent = agentRepository.save(agent);

        User user = modelMapper.map(userCommand, User.class);
        String password;
        if ("admin".equals(userCommand.getUsername()) && demoAdminPassword != null && !demoAdminPassword.isEmpty()) {
            password = demoAdminPassword;
        } else if ("commercial".equals(userCommand.getUsername()) && demoCommercialPassword != null && !demoCommercialPassword.isEmpty()) {
            password = demoCommercialPassword;
        } else {
            password = PasswordUtils.generatePassword(8);
        }
        log.info("Generated password for user {}: {}", userCommand.getUsername(), password);
        user.setPassword(encoder.encode(password));
        user.setRole(roleRepository.findByCode(userCommand.getRole()));
        user.setAgent(agent);
        user = userRepository.save(user);

        try {
            emailService.sendWelcomePasswordEmail(user.getAgent().getFirstname(),user.getAgent().getEmail(), user.getUsername(), password);
        } catch (Exception e) {
            log.error("Error sending email to user: {} with password: {}", user.getUsername(), password);
        }

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public void createClient(UserCommand userCommand) {
        User user = modelMapper.map(userCommand, User.class);
        String password = PasswordUtils.generatePassword(8);
        user.setPassword(encoder.encode(password));
        user.setHasToChangePassword(true);
//        user.setRole(roleRepository.findByCode(userCommand.getRole()));
        user.setClient(userCommand.getClient());
        user = userRepository.save(user);

        try {
            emailService.sendWelcome("cher client",user.getUsername(), password, user.getClient().getEmail());
        } catch (Exception e) {
            log.error("Error sending client creation email: {}", e.getMessage());
        }
        modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto update(UserCommand userCommand, UUID id) {
        User user = userRepository.findById(id).orElseThrow();
        Agent agent = user.getAgent();
        agent.setFirstname(userCommand.getFirstname());
        agent.setLastname(userCommand.getLastname());
        agent.setPhone(userCommand.getPhone());
        agent.setEmail(userCommand.getEmail());
        if (userCommand.getAgenceId() != null) {
            agent.setAgence(agenceRepository.findById(userCommand.getAgenceId()).orElseThrow());
        }
        agentRepository.save(agent);

        user.setRole(roleRepository.findByCode(userCommand.getRole()));
        user = userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updatePassword(UserPasswordUpdateCommand userPasswordUpdateCommand) {
        User user = userRepository.findById(userPasswordUpdateCommand.getUserId()).orElseThrow();
        user.setPassword(encoder.encode(userPasswordUpdateCommand.getNewPassword()));
        user = userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto reinitializePassword(UserPasswordUpdateCommand command, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

        // Vérifie l’ancien mot de passe
        if (!encoder.matches(command.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Ancien mot de passe incorrect");
        }

        user.setPassword(encoder.encode(command.getNewPassword()));
        user.setHasToChangePassword(false);
        user = userRepository.save(user);

        return modelMapper.map(user, UserDto.class);
    }


    @Override
    public UserDto blockUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setLocked(false);
        user = userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto enableUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setLocked(!user.isLocked());
        user = userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public List<String> getPrivileges(User user) {
        Set<Permission> permissions = new HashSet<>();

        if (user.getRole() != null) {
            List<Permission> rolePermissions = rolePermissionRepository.findPermissionByRole(user.getRole().getId());
            if (rolePermissions != null) {
                permissions.addAll(rolePermissions);
            }
        }

        return permissions.stream()
                .map(Permission::getCode)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        return privileges.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
