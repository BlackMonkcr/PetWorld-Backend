package com.example.petworld.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users") // Cambio de "user" a "users" para evitar conflicto con palabra reservada en SQL
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password; // En producción, asegurar que esté encriptada
    private LocalDateTime bornDate; // Fecha de nacimiento del usuario
    private String country; // País del usuario
    private String city; // Ciudad del usuario

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Pet> pets = new ArrayList<>();
}
