package com.adouk.finacombackend.infrastructure.rest.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class UserSessionDto {

    private UUID userSessionId;
    private UserDto userDto;
    private Date startDate;
    private Date endDate;
    private String ipAdress;
    private String location;
}
