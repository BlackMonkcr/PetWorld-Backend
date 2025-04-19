package com.example.petworld.service;

import com.example.petworld.domain.Pet;
import com.example.petworld.domain.Interaction;
import com.example.petworld.domain.User;
import com.example.petworld.dto.Interaction.InteractionResponseDTO;
import com.example.petworld.dto.Pet.PetCreateDTO;
import com.example.petworld.dto.Pet.PetResponseDTO;
import com.example.petworld.dto.Pet.PetSimpleDTO;
import com.example.petworld.dto.User.UserSimpleDTO;
import com.example.petworld.infrastructure.PetRepository;
import com.example.petworld.infrastructure.InteractionRepository;
import com.example.petworld.infrastructure.UserRepository;
import com.example.petworld.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final InteractionRepository interactionRepository;

    @Autowired
    public PetService(PetRepository petRepository,
                      UserRepository userRepository,
                      InteractionRepository interactionRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.interactionRepository = interactionRepository;
    }

    /**
     * Crea una nueva mascota
     * @param petCreateDTO Datos para crear la mascota
     * @return La mascota creada
     */
    @Transactional
    public PetResponseDTO createPet(PetCreateDTO petCreateDTO) {
        // Obtener el usuario actual (esto sería con seguridad implementada)
        // Para este ejemplo asumimos que obtenemos el userId desde el token o sesión
        Long userId = getCurrentUserId();
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Pet pet = new Pet();
        pet.setName(petCreateDTO.getName());
        pet.setDescription(petCreateDTO.getDescription());
        pet.setType(petCreateDTO.getType());
        pet.setImageUrl(petCreateDTO.getImageUrl());

        // Inicializar estados
        pet.setHunger(100);
        pet.setHappiness(100);
        pet.setHealth(100);
        pet.setEnergy(100);

        pet.setCreatedAt(LocalDateTime.now());
        pet.setLastInteraction(LocalDateTime.now());
        pet.setOwner(owner);

        Pet savedPet = petRepository.save(pet);

        return convertToResponseDTO(savedPet);
    }

    /**
     * Obtiene todas las mascotas, con filtros opcionales
     * @param type Tipo de mascota (opcional)
     * @param ownerId ID del dueño (opcional)
     * @return Lista de mascotas
     */
    public List<PetResponseDTO> getAllPets(String type, Long ownerId) {
        List<Pet> pets;

        if (type != null && ownerId != null) {
            pets = petRepository.findByTypeAndOwnerId(type, ownerId);
        } else if (type != null) {
            pets = petRepository.findByType(type);
        } else if (ownerId != null) {
            pets = petRepository.findByOwnerId(ownerId);
        } else {
            pets = petRepository.findAll();
        }

        return pets.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene una mascota por su ID
     * @param id ID de la mascota
     * @return La mascota encontrada
     */
    public PetResponseDTO getPetById(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));

        // Actualizar estados basados en el tiempo transcurrido
        updatePetStates(pet);

        return convertToResponseDTO(pet);
    }

    /**
     * Actualiza una mascota existente
     * @param id ID de la mascota
     * @param petUpdateDTO Datos para actualizar
     * @return La mascota actualizada
     */
    @Transactional
    public PetResponseDTO updatePet(Long id, PetCreateDTO petUpdateDTO) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));

        // Verificar que el usuario actual es el dueño
        verifyOwnership(pet);

        pet.setName(petUpdateDTO.getName());
        pet.setDescription(petUpdateDTO.getDescription());
        pet.setType(petUpdateDTO.getType());
        pet.setImageUrl(petUpdateDTO.getImageUrl());

        Pet updatedPet = petRepository.save(pet);
        return convertToResponseDTO(updatedPet);
    }

    /**
     * Elimina una mascota
     * @param id ID de la mascota
     */
    @Transactional
    public void deletePet(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));

        // Verificar que el usuario actual es el dueño
        verifyOwnership(pet);

        petRepository.delete(pet);
    }

    /**
     * Obtiene las interacciones de una mascota
     * @param petId ID de la mascota
     * @param type Tipo de interacción (opcional)
     * @return Lista de interacciones
     */
    public List<InteractionResponseDTO> getPetInteractions(Long petId, String type) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));

        List<Interaction> interactions;
        if (type != null) {
            interactions = interactionRepository.findByPetIdAndType(petId, type);
        } else {
            interactions = interactionRepository.findByPetId(petId);
        }

        return interactions.stream()
                .map(this::convertToInteractionResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Realiza una interacción con la mascota
     * @param petId ID de la mascota
     * @param interactionType Tipo de interacción
     * @return La mascota actualizada
     */
    @Transactional
    public PetResponseDTO interactWithPet(Long petId, String interactionType) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));

        // Verificar que el usuario actual es el dueño
        verifyOwnership(pet);

        // Actualizar estados basados en el tiempo transcurrido
        updatePetStates(pet);

        // Crear una nueva interacción
        Interaction interaction = new Interaction();
        interaction.setType(interactionType);
        interaction.setTimestamp(LocalDateTime.now());
        interaction.setPet(pet);

        // Aplicar efectos según el tipo de interacción
        switch (interactionType) {
            case "FEED":
                interaction.setValue(30);
                interaction.setDescription("Alimentaste a " + pet.getName());
                pet.setHunger(Math.min(100, pet.getHunger() + 30));
                pet.setEnergy(Math.min(100, pet.getEnergy() + 10));
                break;
            case "PLAY":
                interaction.setValue(25);
                interaction.setDescription("Jugaste con " + pet.getName());
                pet.setHappiness(Math.min(100, pet.getHappiness() + 25));
                pet.setEnergy(Math.max(0, pet.getEnergy() - 20));
                pet.setHunger(Math.max(0, pet.getHunger() - 10));
                break;
            case "HEAL":
                interaction.setValue(40);
                interaction.setDescription("Curaste a " + pet.getName());
                pet.setHealth(Math.min(100, pet.getHealth() + 40));
                break;
            case "PET":
                interaction.setValue(15);
                interaction.setDescription("Acariciaste a " + pet.getName());
                pet.setHappiness(Math.min(100, pet.getHappiness() + 15));
                break;
            default:
                interaction.setValue(5);
                interaction.setDescription("Interactuaste con " + pet.getName());
                pet.setHappiness(Math.min(100, pet.getHappiness() + 5));
        }

        // Guardar la interacción
        interactionRepository.save(interaction);

        // Actualizar la última interacción
        pet.setLastInteraction(LocalDateTime.now());

        // Guardar la mascota actualizada
        Pet updatedPet = petRepository.save(pet);

        return convertToResponseDTO(updatedPet);
    }

    /**
     * Actualiza los estados de la mascota basados en el tiempo transcurrido
     * @param pet La mascota a actualizar
     */
    private void updatePetStates(Pet pet) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastInteraction = pet.getLastInteraction();

        // Calcular horas desde la última interacción
        long hoursSinceLastInteraction = java.time.Duration.between(lastInteraction, now).toHours();

        if (hoursSinceLastInteraction > 0) {
            // Disminuir estados según el tiempo transcurrido
            int hungerDecrease = (int) (hoursSinceLastInteraction * 5); // 5 puntos por hora
            int happinessDecrease = (int) (hoursSinceLastInteraction * 3); // 3 puntos por hora
            int energyIncrease = (int) (hoursSinceLastInteraction * 2); // 2 puntos por hora (recuperación)

            pet.setHunger(Math.max(0, pet.getHunger() - hungerDecrease));
            pet.setHappiness(Math.max(0, pet.getHappiness() - happinessDecrease));
            pet.setEnergy(Math.min(100, pet.getEnergy() + energyIncrease));

            // Si la mascota tiene hambre por mucho tiempo, afecta su salud
            if (pet.getHunger() < 30) {
                int healthDecrease = (int) (hoursSinceLastInteraction * 2);
                pet.setHealth(Math.max(0, pet.getHealth() - healthDecrease));
            }

            // Si la mascota está muy triste, afecta su salud
            if (pet.getHappiness() < 20) {
                int healthDecrease = (int) (hoursSinceLastInteraction);
                pet.setHealth(Math.max(0, pet.getHealth() - healthDecrease));
            }

            // Guardar los cambios
            petRepository.save(pet);
        }
    }

    /**
     * Verifica que el usuario actual es el dueño de la mascota
     * @param pet La mascota a verificar
     */
    private void verifyOwnership(Pet pet) {
        Long currentUserId = getCurrentUserId();
        if (!pet.getOwner().getId().equals(currentUserId)) {
            throw new SecurityException("No tienes permiso para modificar esta mascota");
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
     * Convierte una entidad Pet a DTO
     * @param pet La entidad Pet
     * @return El DTO correspondiente
     */
    private PetResponseDTO convertToResponseDTO(Pet pet) {
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

        UserSimpleDTO ownerDto = new UserSimpleDTO();
        ownerDto.setId(pet.getOwner().getId());
        ownerDto.setUsername(pet.getOwner().getUsername());
        dto.setOwner(ownerDto);

        return dto;
    }

    /**
     * Convierte una entidad Interaction a DTO
     * @param interaction La entidad Interaction
     * @return El DTO correspondiente
     */
    private InteractionResponseDTO convertToInteractionResponseDTO(Interaction interaction) {
        InteractionResponseDTO dto = new InteractionResponseDTO();
        dto.setId(interaction.getId());
        dto.setType(interaction.getType());
        dto.setValue(interaction.getValue());
        dto.setDescription(interaction.getDescription());
        dto.setTimestamp(interaction.getTimestamp());

        PetSimpleDTO petDto = new PetSimpleDTO();
        petDto.setId(interaction.getPet().getId());
        petDto.setName(interaction.getPet().getName());
        petDto.setType(interaction.getPet().getType());
        petDto.setImageUrl(interaction.getPet().getImageUrl());
        dto.setPet(petDto);

        return dto;
    }
}