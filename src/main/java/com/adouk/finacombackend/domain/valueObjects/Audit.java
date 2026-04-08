package com.adouk.finacombackend.domain.valueObjects;

import lombok.Data;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@MappedSuperclass
public class Audit {

    private Boolean deleted;
    private LocalDateTime deletedAt;
    private String deletedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    private String updatedBy;

}
