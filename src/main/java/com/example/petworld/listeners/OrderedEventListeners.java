package com.example.petworld.listeners;

import com.example.petworld.events.PetCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Listeners ordenados que se ejecutan en secuencia específica
 */
@Slf4j
@Component
public class OrderedEventListeners {

    @EventListener
    @Order(1) // Prioridad más alta, se ejecuta primero
    public void logPetCreation(PetCreatedEvent event) {
        log.info("PASO 1: Registrando creación de mascota: {}", event.getPetName());
    }

    @EventListener
    @Order(2) // Se ejecuta después
    public void updateStatistics(PetCreatedEvent event) {
        log.info("PASO 2: Actualizando estadísticas para tipo: {}", event.getPetType());
    }

    @EventListener
    @Order(3) // Se ejecuta al final
    public void sendWelcomeMessage(PetCreatedEvent event) {
        log.info("PASO 3: Enviando mensaje de bienvenida para: {}", event.getPetName());
    }
}