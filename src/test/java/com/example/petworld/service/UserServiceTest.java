package com.example.petworld.service;

import com.example.petworld.domain.User;
import com.example.petworld.dto.User.UserCreateDTO;
import com.example.petworld.dto.User.UserResponseDTO;
import com.example.petworld.exception.ResourceNotFoundException;
import com.example.petworld.infrastructure.UserRepository;
import com.example.petworld.infrastructure.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the UserService class
 * These tests verify the business logic in isolation
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserCreateDTO userCreateDTO;

    @BeforeEach
    public void setup() {
        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");

        // Setup user creation DTO
        userCreateDTO = new UserCreateDTO();
        userCreateDTO.setUsername("testUser");
        userCreateDTO.setEmail("test@example.com");
        userCreateDTO.setPassword("password123");
    }

    @Test
    public void testCreateUser_Success() {
        // Arrange: Configure mock to return our test user
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act: Call the method being tested
        UserResponseDTO result = userService.createUser(userCreateDTO);

        // Assert: Verify the results
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());

        // Verify the repository method was called
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testGetAllUsers() {
        // Arrange: Configure userRepository to return a list of users
        List<User> userList = Arrays.asList(testUser);
        when(userRepository.findAll()).thenReturn(userList);

        // Act: Call the method being tested
        List<UserResponseDTO> result = userService.getAllUsers();

        // Assert: Verify the results
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testUser", result.get(0).getUsername());

        // Verify the repository method was called
        verify(userRepository).findAll();
    }

    @Test
    public void testGetUserById_Success() {
        // Arrange: Configure userRepository to return our test user
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act: Call the method being tested
        UserResponseDTO result = userService.getUserById(1L);

        // Assert: Verify the results
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testUser", result.getUsername());

        // Verify the repository method was called
        verify(userRepository).findById(1L);
    }

    @Test
    public void testGetUserById_NotFound() {
        // Arrange: Configure userRepository to return empty
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert: Call the method and verify it throws the expected exception
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(999L);
        });

        assertTrue(exception.getMessage().contains("Usuario no encontrado"));

        // Verify the repository method was called
        verify(userRepository).findById(999L);
    }

    @Test
    public void testUpdateUser_Success() {
        // Arrange: Setup update DTO with new values
        UserCreateDTO updateDTO = new UserCreateDTO();
        updateDTO.setUsername("updatedUser");
        updateDTO.setEmail("updated@example.com");

        // Create a copy of the test user to represent the updated user
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("updatedUser");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPassword("password123");

        // Configure mocks
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // Act: Call the method being tested
        UserResponseDTO result = userService.updateUser(1L, updateDTO);

        // Assert: Verify the results
        assertNotNull(result);
        assertEquals("updatedUser", result.getUsername());
        assertEquals("updated@example.com", result.getEmail());

        // Verify the repository methods were called
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }
}