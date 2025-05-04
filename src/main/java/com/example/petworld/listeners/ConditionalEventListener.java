package com.example.petworld.listeners;

import com.example.petworld.events.PetCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Listener que procesa eventos solo si cumplen ciertas condiciones
 */
@Slf4j
@Component
public class ConditionalEventListener {

    /**
     * Este método solo se ejecuta para mascotas tipo "Dog"
     */
    @Async("taskExecutor")
    @EventListener(condition = "#event.petType == 'Dog'")
    public void handleDogCreatedEvent(PetCreatedEvent event) {
        log.info("¡Se ha registrado un nuevo perro!: {}", event.getPetName());

        // Procesamiento específico para perros:
        // - Envío de información sobre paseos para perros
        // - Recomendaciones de juguetes específicos
        // - Inscripción automática en programa de vacunación canina
    }

    /**
     * Este método solo se ejecuta para mascotas tipo "Cat"
     */
    @Async("taskExecutor")
    @EventListener(condition = "#event.petType == 'Cat'")
    public void handleCatCreatedEvent(PetCreatedEvent event) {
        log.info("¡Se ha registrado un nuevo gato!: {}", event.getPetName());

        // Lógica específica para gatos
    }
}