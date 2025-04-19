package com.example.petworld.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pets")
@Getter
@Setter
@NoArgsConstructor
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String type; // Tipo de mascota (perro, gato, Pokémon, etc.)
    private String imageUrl; // URL o referencia a la imagen de la mascota

    // Estados de la mascota
    private Integer hunger = 100; // 0-100
    private Integer happiness = 100; // 0-100
    private Integer health = 100; // 0-100
    private Integer energy = 100; // 0-100

    private LocalDateTime lastInteraction; // Último momento de interacción
    private LocalDateTime createdAt; // Fecha de creación/adopción

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<Interaction> interactions = new ArrayList<>();
}
