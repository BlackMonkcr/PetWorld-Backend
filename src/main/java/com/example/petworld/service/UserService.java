package com.example.petworld.service;

import com.example.petworld.domain.User;
import com.example.petworld.domain.Pet;
import com.example.petworld.dto.Pet.PetResponseDTO;
import com.example.petworld.dto.User.UserCreateDTO;
import com.example.petworld.dto.User.UserResponseDTO;
import com.example.petworld.dto.Pet.PetSimpleDTO;
import com.example.petworld.infrastructure.UserRepository;
import com.example.petworld.infrastructure.PetRepository;
import com.example.petworld.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PetRepository petRepository;

    @Autowired
    public UserService(UserRepository userRepository, PetRepository petRepository) {
        this.userRepository = userRepository;
        this.petRepository = petRepository;
    }

    /**
     * Crea un nuevo usuario
     * @param userCreateDTO Datos para crear el usuario
     * @return El usuario creado
     */
    @Transactional
    public UserResponseDTO createUser(UserCreateDTO userCreateDTO) {
        // En un sistema real, verificaríamos si el email ya existe
        // y encriptaríamos la contraseña

        User user = new User();
        user.setUsername(userCreateDTO.getUsername());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(userCreateDTO.getPassword()); // En producción: hash la contraseña

        User savedUser = userRepository.save(user);

        return convertToResponseDTO(savedUser);
    }

    /**
     * Obtiene todos los usuarios
     * @return Lista de usuarios
     */
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un usuario por su ID
     * @param id ID del usuario
     * @return El usuario encontrado
     */
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return convertToResponseDTO(user);
    }

    /**
     * Actualiza un usuario existente
     * @param id ID del usuario
     * @param userUpdateDTO Datos para actualizar
     * @return El usuario actualizado
     */
    @Transactional
    public UserResponseDTO updateUser(Long id, UserCreateDTO userUpdateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Verificar que el usuario actual es quien está haciendo la actualización
        verifyPermission(id);

        user.setUsername(userUpdateDTO.getUsername());
        user.setEmail(userUpdateDTO.getEmail());
        // En un sistema real, solo actualizaríamos la contraseña si se proporciona una nueva
        if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().isEmpty()) {
            user.setPassword(userUpdateDTO.getPassword()); // En producción: hash la contraseña
        }

        User updatedUser = userRepository.save(user);
        return convertToResponseDTO(updatedUser);
    }

    /**
     * Elimina un usuario
     * @param id ID del usuario
     */
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Verificar que el usuario actual es quien está haciendo la eliminación
        // o un administrador
        verifyPermission(id);

        userRepository.delete(user);
    }

    /**
     * Obtiene las mascotas de un usuario
     * @param userId ID del usuario
     * @return Lista de mascotas
     */
    public List<PetResponseDTO> getUserPets(Long userId) {
        // Verificar que el usuario existe
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }

        // Obtener las mascotas del usuario
        List<Pet> pets = petRepository.findByOwnerId(userId);

        return pets.stream()
                .map(this::convertToPetResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Verifica que el usuario actual tiene permiso para modificar el recurso
     * @param userId ID del usuario a modificar
     */
    private void verifyPermission(Long userId) {
        Long currentUserId = getCurrentUserId();
        // En un sistema real, verificaríamos si el usuario actual es el mismo que se intenta
        // modificar o si es un administrador
        if (!userId.equals(currentUserId)) {
            throw new SecurityException("No tienes permiso para modificar este usuario");
        }
    }

    /**
     * Obtiene el ID del usuario actual
     * @return ID del usuario
     */
    private Long getCurrentUserId() {
        // En un sistema real, esto vendría de la autenticación
        // Por ahora, devolvemos un ID fijo para pruebas
        return 1L;
    }

    /**
     * Convierte una entidad User a DTO
     * @param user La entidad User
     * @return El DTO correspondiente
     */
    private UserResponseDTO convertToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());

        // Convertir las mascotas a DTO simplificados
        if (user.getPets() != null) {
            List<PetSimpleDTO> petDtos = user.getPets().stream()
                    .map(pet -> {
                        PetSimpleDTO petDto = new PetSimpleDTO();
                        petDto.setId(pet.getId());
                        petDto.setName(pet.getName());
                        petDto.setType(pet.getType());
                        petDto.setImageUrl(pet.getImageUrl());
                        return petDto;
                    })
                    .collect(Collectors.toList());

            dto.setPets(petDtos);
        }

        return dto;
    }

    /**
     * Convierte una entidad Pet a DTO de respuesta
     * @param pet La entidad Pet
     * @return El DTO correspondiente
     */
    private PetResponseDTO convertToPetResponseDTO(Pet pet) {
        PetResponseDTO dto = new PetResponseDTO();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setDescription(pet.getDescription());
        dto.setType(pet.getType());
        dto.setImageUrl(pet.getImageUrl());
        dto.setHunger(pet.getHunger());
        dto.setHappiness(pet.getHappiness());
        dto.setHealth(pet.getHealth());
        dto.setEnergy(pet.getEnergy());
        dto.setLastInteraction(pet.getLastInteraction());
        dto.setCreatedAt(pet.getCreatedAt());

        // No incluimos el owner para evitar recursión circular

        return dto;
    }
}