package com.example.petworld.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Evento que se dispara cuando se crea una nueva mascota
 */
@Getter
public class PetCreatedEvent extends ApplicationEvent {
    private final Long petId;
    private final String petName;
    private final String petType;

    public PetCreatedEvent(Object source, Long petId, String petName, String petType) {
        super(source);
        this.petId = petId;
        this.petName = petName;
        this.petType = petType;
    }
}