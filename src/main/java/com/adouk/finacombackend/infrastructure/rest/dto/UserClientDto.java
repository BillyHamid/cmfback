package com.adouk.finacombackend.infrastructure.rest.dto;

import com.adouk.finacombackend.domain.aggregates.Agent;
import com.adouk.finacombackend.domain.aggregates.Client;
import com.adouk.finacombackend.domain.aggregates.Role;
import com.adouk.finacombackend.domain.aggregates.User;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class UserClientDto {
    private UUID uuid;
    private Date createdAt;
    private String username;
    private Role role;
    private Client client;
    private boolean locked;
    private boolean hasToChangePassword = true;


    public static UserClientDto toDto(User user) {

        UserClientDto userDTO = new UserClientDto();
        userDTO.setUuid(user.getUuid());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setLocked(user.isLocked());
        userDTO.setHasToChangePassword(user.isHasToChangePassword());

        return userDTO;
    }

}
