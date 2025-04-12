package com.example.petworld.application;

import com.example.petworld.domain.pet;
import com.example.petworld.service.petService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pet")
public class petController {
    private final petService petService;

    @Autowired
    public petController(petService petService) {
        this.petService = petService;
    }

    // === Endpoints ===

    // GET , POST, PUT, DELETE, PATCH

    @PostMapping()
    public ResponseEntity<pet> createPetController(@RequestBody pet newPet) {
        pet createdPet = petService.createPet(newPet);
        return new ResponseEntity<>(createdPet, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<pet>> getAllPetsController() {
        List<pet> pets = petService.getAllPets();
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }
}
