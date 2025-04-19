package com.example.petworld.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "interactions")
@Getter
@Setter
@NoArgsConstructor
public class Interaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // Tipo de interacción: FEED, PLAY, HEAL, PET, etc.
    private Integer value; // Valor de la interacción (cuánto afecta a los estados)
    private String description; // Descripción de la interacción
    private LocalDateTime timestamp; // Momento de la interacción

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;
}
