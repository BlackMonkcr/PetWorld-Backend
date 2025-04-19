package com.example.petworld.dto.User;

import lombok.Getter;
import lombok.Setter;

// DTO para la creación de usuario
@Getter
@Setter
public class UserCreateDTO {
    private String username;
    private String email;
    private String password;
}
