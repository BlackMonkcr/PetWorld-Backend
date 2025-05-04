package com.example.petworld.listeners;

import com.example.petworld.events.PetCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Listener asincrónico para eventos de mascotas
 */
@Slf4j
@Component
public class AsyncPetEventListener {

    /**
     * Este method se ejecuta asincrónicamente cuando se publica un PetCreatedEvent
     */
    @Async("taskExecutor")
    @EventListener
    public void handlePetCreatedEventAsync(PetCreatedEvent event) {
        // Simulamos un procesamiento de larga duración
        try {
            log.info("Iniciando procesamiento asincrónico para mascota: {}", event.getPetName());
            Thread.sleep(2000);
            log.info("[Procesamiento Async] Notificación enviada para: {} después de 2 segundos",
                    event.getPetName());

            // Aquí podrías implementar:
            // - Envío de correos electrónicos de bienvenida
            // - Actualización de estadísticas del sistema
            // - Generación de reportes
            // - Cualquier tarea que requiera tiempo y no bloquee al usuario

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Procesamiento interrumpido para mascota: {}", event.getPetName());
        }
    }
}