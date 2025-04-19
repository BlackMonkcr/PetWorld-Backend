package com.example.petworld.infrastructure;

import com.example.petworld.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByType(String type);
    List<Pet> findByOwnerId(Long ownerId);
    List<Pet> findByTypeAndOwnerId(String type, Long ownerId);
}