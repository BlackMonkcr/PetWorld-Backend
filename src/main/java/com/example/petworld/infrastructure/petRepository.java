package com.example.petworld.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.petworld.domain.pet;

public interface petRepository extends JpaRepository<pet, Long> {}
