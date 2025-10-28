package dev.alejandro.heroquest_backend.auth.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

/**
 * TokenService:
 * Servicio responsable de generar tokens JWT firmados (symmetric HS512),
 * sigue la estructura de Giacomo.
 *
 * - Usa un JwtEncoder (de Spring Security) para crear el token.
 * - Añade claims útiles: issuer, issuedAt, subject, expiresAt y scope.
 * - La duración del token es fija (1 hora).
 * 
 * Requisitos:
  * - Debo tener un bean JwtEncoder configurado (en SecurityConfig u otro @Configuration).
 *   En los ejemplos de Giacomo usa NimbusJwtEncoder con una clave simétrica.
 */
@Service
public class TokenService {

    // JwtEncoder inyectado por Spring. Debe estar configurado (NimbusJwtEncoder con clave).
    private final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Genera un token JWT para el usuario autenticado.
     * @param authentication Objeto Authentication (proviene de Spring Security) con nombre.
     * @return token JWT en formato String.
     */
    public String generateToken(Authentication authentication) {

        Instant now = Instant.now();

        // Extraemos los "scopes" (roles o authorities) del usuario.
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> !authority.startsWith("ROLE"))
                .collect(Collectors.joining(" "));

        // Creamos los claims del token (emisor, usuario, expiración, etc.)
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("heroquest-backend")
                .issuedAt(now)
                .subject(authentication.getName())
                .expiresAt(now.plus(1, ChronoUnit.HOURS)) // 1 hora de duración
                .claim("scope", scope)
                .build();

        // Cabecera JWS con algoritmo HS512 (simétrico)
        var encoderParameters = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS512).build(),
                claims
        );

        // Codificamos y devolvemos el token JWT
        return this.jwtEncoder.encode(encoderParameters).getTokenValue();
    }
}