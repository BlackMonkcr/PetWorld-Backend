package com.example.petworld.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.petworld.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {}
