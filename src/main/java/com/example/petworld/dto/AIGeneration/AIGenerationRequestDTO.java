package com.example.petworld.dto.AIGeneration;

import lombok.Getter;
import lombok.Setter;

// DTO para solicitar generación de IA
@Getter
@Setter
public class AIGenerationRequestDTO {
    private String prompt;
}