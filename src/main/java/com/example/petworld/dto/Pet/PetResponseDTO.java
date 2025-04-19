package com.example.petworld.dto.Pet;

import java.time.LocalDateTime;
import com.example.petworld.dto.User.UserSimpleDTO;
import lombok.Getter;
import lombok.Setter;

// DTO para la respuesta completa de mascota
@Getter
@Setter
public class PetResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String type;
    private String imageUrl;
    private Integer hunger;
    private Integer happiness;
    private Integer health;
    private Integer energy;
    private LocalDateTime lastInteraction;
    private LocalDateTime createdAt;
    private UserSimpleDTO owner;
}
