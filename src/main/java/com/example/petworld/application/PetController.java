package com.example.petworld.application;

import com.example.petworld.dto.Interaction.InteractionResponseDTO;
import com.example.petworld.dto.Pet.PetCreateDTO;
import com.example.petworld.dto.Pet.PetResponseDTO;
import com.example.petworld.dto.ResponseDTO;
import com.example.petworld.exception.ResourceNotFoundException;
import com.example.petworld.security.UserDetailsImpl;
import com.example.petworld.service.PetService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/pets")
public class PetController {
    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<PetResponseDTO>> createPet(
        @RequestBody PetCreateDTO petCreateDTO,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            PetResponseDTO createdPet = petService.createPet(petCreateDTO, userDetails.getId());
            ResponseDTO<PetResponseDTO> response = new ResponseDTO<>();
            response.setData(createdPet);
            response.setMessage("Pet created successfully");
            response.setStatus(HttpStatus.CREATED.value());
            response.setError(null);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage()); // Print the error message to the log
            ResponseDTO<PetResponseDTO> response = new ResponseDTO<>();
            response.setData(null);
            response.setMessage("Invalid input data");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setError(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found: {}", e.getMessage());
            ResponseDTO<PetResponseDTO> response = new ResponseDTO<>();
            response.setData(null);
            response.setMessage("Resource Not Found");
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setError(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            log.error("Error creating pet: {}", e.getMessage());
            ResponseDTO<PetResponseDTO> response = new ResponseDTO<>();
            response.setData(null);
            response.setMessage("Internal Server Error");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setError(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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