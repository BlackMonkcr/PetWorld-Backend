package com.example.petworld.service;

import com.example.petworld.domain.Pet;
import com.example.petworld.domain.User;
import com.example.petworld.dto.Pet.PetCreateDTO;
import com.example.petworld.dto.Pet.PetResponseDTO;
import com.example.petworld.exception.ResourceNotFoundException;
import com.example.petworld.infrastructure.InteractionRepository;
import com.example.petworld.infrastructure.PetRepository;
import com.example.petworld.infrastructure.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the PetService class
 * These tests verify that the PetService behaves as expected
 * without actually connecting to a database
 * CONCEPTS:
 * when():
 * Configura qué hará el mock cuando se llame a un method.
 * Define el comportamiento previsto.
 * verify():
 * Verifica que el mock fue llamado de cierta manera.
 * Valida las interacciones con el mock.
 */
@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

    // Mocks for the repositories that PetService depends on
    @Mock
    private PetRepository petRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InteractionRepository interactionRepository;

    // The service we're testing, with mocks injected
    @InjectMocks
    private PetService petService;

    // Test data
    private User testUser;
    private Pet testPet;
    private PetCreateDTO petCreateDTO;

    @BeforeEach
    public void setup() {
        // Setup a test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setEmail("test@example.com");

        // Setup a test pet
        testPet = new Pet();
        testPet.setId(1L);
        testPet.setName("Fluffy");
        testPet.setType("Cat");
        testPet.setDescription("A cute fluffy cat");
        testPet.setImageUrl("http://example.com/fluffy.jpg");
        testPet.setHunger(100);
        testPet.setHappiness(100);
        testPet.setHealth(100);
        testPet.setEnergy(100);
        testPet.setCreatedAt(LocalDateTime.now());
        testPet.setLastInteraction(LocalDateTime.now());
        testPet.setOwner(testUser);

        // Setup pet creation DTO
        petCreateDTO = new PetCreateDTO();
        petCreateDTO.setName("Fluffy");
        petCreateDTO.setType("Cat");
        petCreateDTO.setDescription("A cute fluffy cat");
        petCreateDTO.setImageUrl("http://example.com/fluffy.jpg");
    }

    @Test
    public void testCreatePet_Success() {
        // Arrange: Configure mocks to return our test data
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(petRepository.save(any(Pet.class))).thenReturn(testPet);

        // Act: Call the method being tested
        PetResponseDTO result = petService.createPet(petCreateDTO);

        // Assert: Verify the results
        assertNotNull(result);
        assertEquals("Fluffy", result.getName());
        assertEquals("Cat", result.getType());

        // Verify the repository methods were called
        verify(userRepository).findById(anyLong());
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    public void testCreatePet_UserNotFound() {
        // Arrange: Configure user repository to return empty
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert: Call the method and verify it throws the expected exception
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            petService.createPet(petCreateDTO);
        });

        assertTrue(exception.getMessage().contains("Usuario no encontrado"));

        // Verify the repository method was called
        verify(userRepository).findById(anyLong());
        // Verify that petRepository.save was NOT called
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    public void testGetAllPets_NoFilters() {
        // Arrange: Configure petRepository to return a list of pets
        List<Pet> petList = Arrays.asList(testPet);
        when(petRepository.findAll()).thenReturn(petList);

        // Act: Call the method being tested
        List<PetResponseDTO> result = petService.getAllPets(null, null);

        // Assert: Verify the results
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Fluffy", result.get(0).getName());

        // Verify the repository method was called
        verify(petRepository).findAll();
    }

    @Test
    public void testGetAllPets_WithTypeFilter() {
        // Arrange: Configure petRepository to return filtered pets
        List<Pet> petList = Arrays.asList(testPet);
        when(petRepository.findByType("Cat")).thenReturn(petList);

        // Act: Call the method with type filter
        List<PetResponseDTO> result = petService.getAllPets("Cat", null);

        // Assert: Verify the results
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cat", result.get(0).getType());

        // Verify the repository method was called with type filter
        verify(petRepository).findByType("Cat");
    }

    @Test
    public void testGetPetById_Success() {
        // Arrange: Configure petRepository to return our test pet
        when(petRepository.findById(1L)).thenReturn(Optional.of(testPet));

        // Act: Call the method being tested
        PetResponseDTO result = petService.getPetById(1L);

        // Assert: Verify the results
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Fluffy", result.getName());

        // Verify the repository method was called
        verify(petRepository).findById(1L);
    }

    @Test
    public void testGetPetById_NotFound() {
        // Arrange: Configure petRepository to return empty
        when(petRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert: Call the method and verify it throws the expected exception
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            petService.getPetById(999L);
        });

        assertTrue(exception.getMessage().contains("Mascota no encontrada"));

        // Verify the repository method was called
        verify(petRepository).findById(999L);
    }

    @Test
    public void testInteractWithPet_Feed() {
        // Arrange: Configure mocks
        when(petRepository.findById(1L)).thenReturn(Optional.of(testPet));
        when(petRepository.save(any(Pet.class))).thenReturn(testPet);

        // Set initial values for testing the change
        testPet.setHunger(50);

        // Act: Call the method being tested
        PetResponseDTO result = petService.interactWithPet(1L, "FEED");

        // Assert: Verify the pet's hunger increased
        assertEquals(80, testPet.getHunger()); // Should be 50 + 30

        // Verify repository methods were called
        verify(petRepository).findById(1L);
        verify(interactionRepository).save(any());
        verify(petRepository).save(testPet);
    }
}