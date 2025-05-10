package com.example.petworld.controller;

import com.example.petworld.application.PetController;
import com.example.petworld.dto.Pet.PetCreateDTO;
import com.example.petworld.dto.Pet.PetResponseDTO;
import com.example.petworld.dto.User.UserSimpleDTO;
import com.example.petworld.exception.ResourceNotFoundException;
import com.example.petworld.security.UserDetailsImpl;
import com.example.petworld.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the PetController
 * These tests verify that the controller properly handles HTTP requests and responses
 */
@ExtendWith(MockitoExtension.class)
public class PetControllerTest {

    @Mock
    private PetService petService;
    
    @Mock
    private UserDetailsImpl userDetails;

    @InjectMocks
    private PetController petController;

    private PetCreateDTO petCreateDTO;
    private PetResponseDTO petResponseDTO;

    @BeforeEach
    public void setup() {
        // Setup test data
        petCreateDTO = new PetCreateDTO();
        petCreateDTO.setName("Fluffy");
        petCreateDTO.setType("Cat");
        petCreateDTO.setDescription("A cute fluffy cat");
        petCreateDTO.setImageUrl("http://example.com/fluffy.jpg");

        petResponseDTO = new PetResponseDTO();
        petResponseDTO.setId(1L);
        petResponseDTO.setName("Fluffy");
        petResponseDTO.setType("Cat");
        petResponseDTO.setDescription("A cute fluffy cat");
        petResponseDTO.setImageUrl("http://example.com/fluffy.jpg");
        petResponseDTO.setHunger(100);
        petResponseDTO.setHappiness(100);
        petResponseDTO.setHealth(100);
        petResponseDTO.setEnergy(100);
        petResponseDTO.setLastInteraction(LocalDateTime.now());
        petResponseDTO.setCreatedAt(LocalDateTime.now());

        UserSimpleDTO owner = new UserSimpleDTO();
        owner.setId(1L);
        owner.setUsername("testUser");
        petResponseDTO.setOwner(owner);
        
        // Configure mock user details
        when(userDetails.getId()).thenReturn(1L);
    }

    @Test
    public void testCreatePet_Success() {
        // Arrange: Configure mock to return test data
        when(petService.createPet(any(PetCreateDTO.class), 1L)).thenReturn(petResponseDTO);

        // Act: Call the controller method
        ResponseEntity<?> response = petController.createPet(petCreateDTO, userDetails);

        // Assert: Verify the response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        // Verify service method was called
        verify(petService).createPet(any(PetCreateDTO.class), 1L);
    }

    @Test
    public void testCreatePet_ServiceThrowsException() {
        // Arrange: Make service throw an exception
        when(petService.createPet(any(PetCreateDTO.class), 1L))
                .thenThrow(new IllegalArgumentException("Invalid input"));

        // Act: Call the controller method
        ResponseEntity<?> response = petController.createPet(petCreateDTO, userDetails);

        // Assert: Verify error handling
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verify service method was called
        verify(petService).createPet(any(PetCreateDTO.class), 1L);
    }

    @Test
    public void testGetAllPets() {
        // Arrange: Configure mock to return list of pets
        List<PetResponseDTO> petList = Arrays.asList(petResponseDTO);
        when(petService.getAllPets(any(), any())).thenReturn(petList);

        // Act: Call the controller method
        ResponseEntity<List<PetResponseDTO>> response = petController.getAllPets(null, null);

        // Assert: Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        // Verify service method was called
        verify(petService).getAllPets(null, null);
    }

    @Test
    public void testGetPetById_Success() {
        // Arrange: Configure mock to return a pet
        when(petService.getPetById(1L)).thenReturn(petResponseDTO);

        // Act: Call the controller method
        ResponseEntity<PetResponseDTO> response = petController.getPetById(1L);

        // Assert: Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());

        // Verify service method was called
        verify(petService).getPetById(1L);
    }

    @Test
    public void testGetPetById_NotFound() {
        // Arrange: Make service throw exception
        when(petService.getPetById(999L)).thenThrow(new ResourceNotFoundException("Mascota no encontrada"));

        // Act & Assert: Call method and verify exception handling
        assertThrows(ResourceNotFoundException.class, () -> {
            petController.getPetById(999L);
        });

        // Verify service method was called
        verify(petService).getPetById(999L);
    }

    @Test
    public void testFeedPet() {
        // Arrange: Configure mock to return updated pet
        when(petService.interactWithPet(1L, "FEED")).thenReturn(petResponseDTO);

        // Act: Call the controller method
        ResponseEntity<PetResponseDTO> response = petController.feedPet(1L);

        // Assert: Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Verify service method was called with correct parameters
        verify(petService).interactWithPet(1L, "FEED");
    }
}