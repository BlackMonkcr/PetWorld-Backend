package com.example.petworld.dto.Pet;

import lombok.Getter;
import lombok.Setter;

// DTO para actualizar estado de mascota
@Getter
@Setter
public class PetUpdateStateDTO {
    private Integer hunger;
    private Integer happiness;
    private Integer health;
    private Integer energy;
}