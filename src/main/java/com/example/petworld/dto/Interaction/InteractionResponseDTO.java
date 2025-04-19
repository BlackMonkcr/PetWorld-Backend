package com.example.petworld.dto.Interaction;


import com.example.petworld.dto.Pet.PetSimpleDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// DTO para la respuesta de interacción
@Getter
@Setter
public class InteractionResponseDTO {
    private Long id;
    private String type;
    private Integer value;
    private String description;
    private LocalDateTime timestamp;
    private PetSimpleDTO pet;
}