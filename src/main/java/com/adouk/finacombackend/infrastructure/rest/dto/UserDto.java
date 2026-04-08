package com.adouk.finacombackend.infrastructure.rest.dto;

import com.adouk.finacombackend.domain.aggregates.Agence;
import com.adouk.finacombackend.domain.aggregates.Agent;
import com.adouk.finacombackend.domain.aggregates.Role;
import com.adouk.finacombackend.domain.aggregates.User;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class UserDto {
    private UUID uuid;
    private Date createdAt;
    private String username;
    private Role role;
    private Agent agent;
    private boolean locked;
    private Date lastAuthDate;

    private boolean hasToChangePassword = true;


    public static UserDto toDto(User user) {

        UserDto userDTO = new UserDto();
        userDTO.setUuid(user.getUuid());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setLocked(user.isLocked());
        userDTO.setHasToChangePassword(user.isHasToChangePassword());

        return userDTO;
    }

}
