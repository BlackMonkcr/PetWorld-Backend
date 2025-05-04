package com.example.petworld.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * Clase que genera y valida tokens JWT
 */
@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret:mysupersecretkey}")
    private String jwtSecret;

    @Value("${app.jwt.expiration.ms:86400000}") // 24 horas por defecto
    private int jwtExpirationMs;

    /**
     * Genera un token JWT a partir de la autenticación del usuario
     */
    public String generateToken(Authentication authentication) {
        // El principal es UserDetails cargado en UserDetailsServiceImpl
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername()) // El username es el email
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Obtiene el email del usuario a partir del token JWT
     */
    public String getUserEmailFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * Valida un token JWT
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            // Token JWT mal formado
            // logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            // Token JWT caducado
            // logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            // Token JWT no soportado
            // logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            // La cadena claims del JWT está vacía
            // logger.error("JWT claims string is empty.");
        }
        return false;
    }

    /**
     * Obtiene la clave para firmar el token
     */
    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
