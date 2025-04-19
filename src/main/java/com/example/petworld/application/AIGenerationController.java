package com.example.petworld.application;

import com.example.petworld.domain.AIGeneration;
import com.example.petworld.service.AIGenerationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aigeneration")
public class AIGenerationController {
    private final AIGenerationService ai_generationService;

    @Autowired
    public AIGenerationController(AIGenerationService ai_generationService) {
        this.ai_generationService = ai_generationService;
    }

    // === Endpoints ===
}
