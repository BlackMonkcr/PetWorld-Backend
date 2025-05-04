package com.example.petworld.dto.auth;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para la respuesta después de un login exitoso
 */
@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long userId;
    private String username;
}