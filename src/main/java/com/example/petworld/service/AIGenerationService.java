package com.example.petworld.service;

import com.example.petworld.domain.AIGeneration;
import com.example.petworld.infrastructure.AIGenerationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AIGenerationService {
    private final AIGenerationRepository ai_generationRepository;

    @Autowired
    public AIGenerationService(AIGenerationRepository ai_generationRepository) {
        this.ai_generationRepository = ai_generationRepository;
    }
    
    // === Functions of service ===
}
