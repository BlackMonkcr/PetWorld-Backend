package com.example.petworld.infrastructure;

import com.example.petworld.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    // Buscar mascotas por tipo
    List<Pet> findByType(String type);

    // Buscar mascotas por ID del dueño
    List<Pet> findByOwnerId(Long ownerId);

    // Buscar mascotas por tipo y ID del dueño
    List<Pet> findByTypeAndOwnerId(String type, Long ownerId);
}