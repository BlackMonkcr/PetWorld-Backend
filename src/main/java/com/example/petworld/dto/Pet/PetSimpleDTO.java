package com.example.petworld.dto.Pet;

import lombok.Getter;
import lombok.Setter;

// DTO simplificado para listas o referencias
@Getter
@Setter
public class PetSimpleDTO {
    private Long id;
    private String name;
    private String type;
    private String imageUrl;
}

