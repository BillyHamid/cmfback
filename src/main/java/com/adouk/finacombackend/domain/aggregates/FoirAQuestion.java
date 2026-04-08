package com.adouk.finacombackend.domain.aggregates;

import com.adouk.finacombackend.domain.valueObjects.Audit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "foir_a_question")
@Getter
@Setter
public class FoirAQuestion extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String libelleQuestion;
    private String reponseQuestion;
}