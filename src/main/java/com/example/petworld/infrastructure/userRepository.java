package com.example.petworld.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.petworld.domain.user;

public interface userRepository extends JpaRepository<user, Long> {}
