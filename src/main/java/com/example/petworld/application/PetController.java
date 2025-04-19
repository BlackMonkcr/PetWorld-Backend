package com.example.petworld.application;

import com.example.petworld.dto.Interaction.InteractionResponseDTO;
import com.example.petworld.dto.Pet.PetCreateDTO;
import com.example.petworld.dto.Pet.PetResponseDTO;
import com.example.petworld.service.PetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {
    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public ResponseEntity<PetResponseDTO> createPet(@RequestBody PetCreateDTO petCreateDTO) {
        PetResponseDTO createdPet = petService.createPet(petCreateDTO);
        return new ResponseEntity<>(createdPet, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PetResponseDTO>> getAllPets(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long ownerId) {
        List<PetResponseDTO> pets = petService.getAllPets(type, ownerId);
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetResponseDTO> getPetById(@PathVariable Long id) {
        PetResponseDTO pet = petService.getPetById(id);
        return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponseDTO> updatePet(
            @PathVariable Long id,
            @RequestBody PetCreateDTO petUpdateDTO) {
        PetResponseDTO updatedPet = petService.updatePet(id, petUpdateDTO);
        return new ResponseEntity<>(updatedPet, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/interactions")
    public ResponseEntity<List<InteractionResponseDTO>> getPetInteractions(
            @PathVariable Long id,
            @RequestParam(required = false) String type) {
        List<InteractionResponseDTO> interactions = petService.getPetInteractions(id, type);
        return new ResponseEntity<>(interactions, HttpStatus.OK);
    }

    @PostMapping("/{id}/feed")
    public ResponseEntity<PetResponseDTO> feedPet(@PathVariable Long id) {
        PetResponseDTO pet = petService.interactWithPet(id, "FEED");
        return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    @PostMapping("/{id}/play")
    public ResponseEntity<PetResponseDTO> playWithPet(@PathVariable Long id) {
        PetResponseDTO pet = petService.interactWithPet(id, "PLAY");
        return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    @PostMapping("/{id}/heal")
    public ResponseEntity<PetResponseDTO> healPet(@PathVariable Long id) {
        PetResponseDTO pet = petService.interactWithPet(id, "HEAL");
        return new ResponseEntity<>(pet, HttpStatus.OK);
    }
}