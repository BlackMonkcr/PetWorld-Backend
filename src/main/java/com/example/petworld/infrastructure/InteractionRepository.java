package com.example.petworld.infrastructure;

import com.example.petworld.domain.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Long> {

    List<Interaction> findByPetId(Long petId);
    List<Interaction> findByPetIdAndType(Long petId, String type);
    List<Interaction> findByPetIdOrderByTimestampDesc(Long petId);
    Long countByPetIdAndType(Long petId, String type);
}