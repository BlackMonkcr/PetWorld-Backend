package com.example.petworld.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_generations")
@Getter
@Setter
@NoArgsConstructor
public class AIGeneration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prompt; // Entrada del usuario para generar la mascota
    private String result; // Resultado de la generación
    private LocalDateTime timestamp; // Momento de la generación

    @OneToOne
    @JoinColumn(name = "pet_id")
    private Pet generatedPet; // Mascota generada (si se guardó)

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Usuario que realizó la generación
}