package com.adouk.finacombackend.domain.aggregates;

import com.adouk.finacombackend.domain.valueObjects.Audit;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.LazyInitializationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "agences")
public class Agence extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;
    private String agencyName;
    private String agencyAdress;
    private String agencyPhone1;
    private String agencyPhone2;
    private String agencyLongitude;
    private String agencyLatitude;
    @ManyToOne
    @JsonIgnoreProperties({"agence"}) // Ignore uniquement le champ agence de l'agent
    private Agent gestionnaire;
    private boolean active = true;
}
