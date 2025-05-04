package com.example.petworld.listeners;

import com.example.petworld.events.PetCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener sincrónico que procesa eventos de mascotas
 */
@Slf4j
@Component
public class PetEventListener {

    /**
     * Este method se ejecuta sincrónicamente cuando se publica un PetCreatedEvent
     */
    @EventListener
    public void handlePetCreatedEvent(PetCreatedEvent event) {
        log.info("Nueva mascota creada: {} (ID: {}, Tipo: {})",
                event.getPetName(), event.getPetId(), event.getPetType());

        // Aquí puedes agregar lógica sincrónica, como validaciones
        // o actualizaciones que deban ocurrir inmediatamente
    }
}