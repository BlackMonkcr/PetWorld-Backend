package com.example.petworld.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Maneja las excepciones de autenticación, retornando un error 401 cuando
 * un usuario intenta acceder a un recurso protegido sin autenticación
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // Enviar respuesta de error de no autorizado
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                "Error: No autorizado. " + authException.getMessage());
    }
}