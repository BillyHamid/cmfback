package com.adouk.finacombackend.Application.services.Interfaces;

import com.adouk.finacombackend.infrastructure.rest.dto.ClientCountByTypeDto;
import com.adouk.finacombackend.infrastructure.rest.dto.CountDto;

import java.util.List;
import java.util.UUID;

public interface StatistiqueService {

    CountDto getDossierCount();
    CountDto getClientCount();
    ClientCountByTypeDto getCLientCountByType();


}
