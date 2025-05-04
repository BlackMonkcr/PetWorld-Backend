package com.example.petworld.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.petworld.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Agregar method para buscar por email
    Optional<User> findByEmail(String email);

    // Verificar si existe un usuario con el email dado
    Boolean existsByEmail(String email);
}
