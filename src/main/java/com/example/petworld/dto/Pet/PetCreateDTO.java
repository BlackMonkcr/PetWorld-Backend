package com.example.petworld.dto.Pet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// DTO para la creación de mascota
@Getter
@Setter
public class PetCreateDTO {
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @Size(min = 10, max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    @NotBlank(message = "Type cannot be blank")
    private String type;
    @NotBlank(message = "Image URL cannot be blank")
    private String imageUrl;
}
