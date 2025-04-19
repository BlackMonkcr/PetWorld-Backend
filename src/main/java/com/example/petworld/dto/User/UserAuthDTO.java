package com.example.petworld.dto.User;

import lombok.Getter;
import lombok.Setter;

// DTO para la autenticación
@Getter
@Setter
public class UserAuthDTO {
    private String email;
    private String password;
}
