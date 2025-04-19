package com.example.petworld.dto.User;

import lombok.Getter;
import lombok.Setter;

// DTO para token JWT (si implementas seguridad)
@Getter
@Setter
public class JwtResponseDTO {
    private String token;
    private String type = "Bearer";
    private Long userId;
    private String username;
}
