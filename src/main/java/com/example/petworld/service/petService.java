package com.example.petworld.service;

import com.example.petworld.domain.pet;
import com.example.petworld.infrastructure.petRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class petService {
    private final petRepository petRepository;

    @Autowired
    public petService(petRepository petRepository) {
        this.petRepository = petRepository;
    }
    
    // === Functions of service ===

    public pet createPet(pet newPet) {
        return petRepository.save(newPet);
        // ORM -> petRepository.save(newPet);
        // SQL -> INSERT INTO pet (name, description) VALUES (newPet.getName(), newPet.getDescription());
    }

    public List<pet> getAllPets() {
        return petRepository.findAll();
        // ORM -> petRepository.findAll();
        // SQL -> SELECT * FROM pet;
    }
}
