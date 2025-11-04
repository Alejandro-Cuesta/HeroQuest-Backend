package dev.alejandro.heroquest_backend.auth;

import dev.alejandro.heroquest_backend.auth.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateToken_ShouldReturnToken() {
        // Mock del nombre del usuario
        when(authentication.getName()).thenReturn("user");

        // Creamos la lista de GrantedAuthority explícitamente
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> "ROLE_USER");
        authorities.add(() -> "read:hero");

        // Usamos doReturn para evitar problema de tipos genéricos en Mockito
        doReturn(authorities).when(authentication).getAuthorities();

        // Mock del JWT retornado por JwtEncoder
        Jwt jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn("token123");
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        // Ejecutamos método
        String token = tokenService.generateToken(authentication);

        // Comprobamos resultado
        assertEquals("token123", token);
    }
}