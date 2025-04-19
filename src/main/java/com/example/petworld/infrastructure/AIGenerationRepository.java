package com.example.petworld.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.petworld.domain.AIGeneration;

public interface AIGenerationRepository extends JpaRepository<AIGeneration, Long> {}
