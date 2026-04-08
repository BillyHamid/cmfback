package com.adouk.finacombackend.domain.aggregates;

import com.adouk.finacombackend.domain.valueObjects.Audit;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
@DiscriminatorColumn(name = "type_client", discriminatorType = DiscriminatorType.STRING)
public class Client extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    protected String numeroClient;
    protected String idSib;
    protected String adresse;
    protected String libelle;
    protected String email;
    protected String telephone1;
    protected String telephone2;
    protected String bp;
    protected String mentionParticuliere;
    @Enumerated(EnumType.STRING)
    protected ClientStatus kycStatus = ClientStatus.PENDING;
    protected Boolean kycValidated = false;

    @ManyToOne
    protected User validateur;

    @ManyToOne
    protected Agence agenceRattachee;

    public Client() {}
}


