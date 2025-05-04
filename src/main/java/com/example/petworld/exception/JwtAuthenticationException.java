package com.example.petworld.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Excepción personalizada para problemas con la autenticación JWT
 */
public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(String message) {
        super(message);
    }

    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
