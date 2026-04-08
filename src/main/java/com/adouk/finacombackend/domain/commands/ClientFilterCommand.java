package com.adouk.finacombackend.domain.commands;

import com.adouk.finacombackend.domain.aggregates.ClientStatus;
import lombok.Data;

@Data
public class ClientFilterCommand {
    protected String search;
    protected ClientStatus status;

}
