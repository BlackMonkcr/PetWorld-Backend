package com.example.petworld.dto.Interaction;

import lombok.Getter;
import lombok.Setter;

// DTO para crear una interacción
@Getter
@Setter
public class InteractionCreateDTO {
    private String type;
    private Long petId;
}

