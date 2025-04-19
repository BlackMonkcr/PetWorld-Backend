package com.example.petworld.dto.User;

import com.example.petworld.dto.Pet.PetSimpleDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// DTO para la respuesta de usuario
@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private List<PetSimpleDTO> pets;
}
