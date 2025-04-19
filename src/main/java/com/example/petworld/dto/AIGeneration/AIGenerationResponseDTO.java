package com.example.petworld.dto.AIGeneration;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// DTO para la respuesta de generación de IA
@Getter
@Setter
public class AIGenerationResponseDTO {
    private Long id;
    private String prompt;
    private String result;
    private LocalDateTime timestamp;
}
