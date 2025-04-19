package com.example.petworld.service;

import com.example.petworld.domain.Interaction;
import com.example.petworld.infrastructure.InteractionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InteractionService {
    private final InteractionRepository interactionRepository;

    @Autowired
    public InteractionService(InteractionRepository interactionRepository) {
        this.interactionRepository = interactionRepository;
    }
    
    // === Functions of service ===
}
