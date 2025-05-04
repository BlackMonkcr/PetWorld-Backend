package com.example.petworld.integration;

import com.example.petworld.domain.User;
import com.example.petworld.dto.Pet.PetCreateDTO;
import com.example.petworld.infrastructure.PetRepository;
import com.example.petworld.infrastructure.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the PetController
 * These tests verify the entire application stack, including HTTP requests,
 * controllers, services, and database interactions.
 *
 * Note: For these tests to run, you need to set up a test database profile
 * in application-test.properties or application-test.yml
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Use test profile with H2 or test PostgreSQL instance
@Transactional // Roll back transactions after each test
public class PetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    private User testUser;
    private PetCreateDTO petCreateDTO;

    @BeforeEach
    public void setup() {
        // Clean repositories before each test
        petRepository.deleteAll();
        userRepository.deleteAll();

        // Create a test user in the database
        testUser = new User();
        testUser.setUsername("integrationTestUser");
        testUser.setEmail("integration@test.com");
        testUser.setPassword("password123");
        testUser.setBornDate(LocalDateTime.now().minusYears(25));
        testUser.setCountry("Test Country");
        testUser.setCity("Test City");
        userRepository.save(testUser);

        // Create a test pet DTO
        petCreateDTO = new PetCreateDTO();
        petCreateDTO.setName("TestPet");
        petCreateDTO.setType("TestType");
        petCreateDTO.setDescription("This is a test pet for integration testing");
        petCreateDTO.setImageUrl("http://example.com/test.jpg");
    }

    @Test
    public void testCreatePet() throws Exception {
        // Convert DTO to JSON
        String petJson = objectMapper.writeValueAsString(petCreateDTO);

        // Perform POST request and verify response
        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name", is("TestPet")))
                .andExpect(jsonPath("$.data.type", is("TestType")))
                .andExpect(jsonPath("$.data.hunger", is(100)))
                .andExpect(jsonPath("$.data.happiness", is(100)))
                .andExpect(jsonPath("$.message", is("Pet created successfully")));
    }

    @Test
    public void testGetAllPets() throws Exception {
        // First create a pet
        String petJson = objectMapper.writeValueAsString(petCreateDTO);
        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson))
                .andExpect(status().isCreated());

        // Now get all pets and verify response
        mockMvc.perform(get("/api/pets"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name", is("TestPet")));
    }

    @Test
    public void testGetPetById() throws Exception {
        // First create a pet
        String petJson = objectMapper.writeValueAsString(petCreateDTO);
        String responseJson = mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Extract the pet ID from the response
        String petId = objectMapper.readTree(responseJson)
                .path("data")
                .path("id")
                .asText();

        // Now get the pet by ID and verify response
        mockMvc.perform(get("/api/pets/" + petId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TestPet")))
                .andExpect(jsonPath("$.type", is("TestType")));
    }

    @Test
    public void testUpdatePet() throws Exception {
        // First create a pet
        String petJson = objectMapper.writeValueAsString(petCreateDTO);
        String responseJson = mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Extract the pet ID from the response
        String petId = objectMapper.readTree(responseJson)
                .path("data")
                .path("id")
                .asText();

        // Create updated pet DTO
        PetCreateDTO updatedPet = new PetCreateDTO();
        updatedPet.setName("UpdatedTestPet");
        updatedPet.setType("UpdatedType");
        updatedPet.setDescription("This pet has been updated");
        updatedPet.setImageUrl("http://example.com/updated.jpg");

        // Update the pet and verify response
        mockMvc.perform(put("/api/pets/" + petId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPet)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("UpdatedTestPet")))
                .andExpect(jsonPath("$.type", is("UpdatedType")));
    }

    @Test
    public void testDeletePet() throws Exception {
        // First create a pet
        String petJson = objectMapper.writeValueAsString(petCreateDTO);
        String responseJson = mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Extract the pet ID from the response
        String petId = objectMapper.readTree(responseJson)
                .path("data")
                .path("id")
                .asText();

        // Delete the pet and verify response
        mockMvc.perform(delete("/api/pets/" + petId))
                .andDo(print())
                .andExpect(status().isNoContent());

        // Verify pet is deleted by trying to get it
        mockMvc.perform(get("/api/pets/" + petId))
                .andExpect(status().isNotFound());
    }
}