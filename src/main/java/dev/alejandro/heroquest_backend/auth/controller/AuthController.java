package dev.alejandro.heroquest_backend.auth.controller;

import dev.alejandro.heroquest_backend.auth.dto.UserDTOResponse;
import dev.alejandro.heroquest_backend.auth.dto.UserLoginDTORequest;
import dev.alejandro.heroquest_backend.auth.dto.UserRegisterDTORequest;
import dev.alejandro.heroquest_backend.auth.service.AuthService;
import dev.alejandro.heroquest_backend.auth.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST que gestiona los endpoints públicos relacionados con autenticación:
 *  - /register  Registro de nuevo usuario.
 *  - /login  Validación de credenciales (sin JWT todavía).
 *  - /token  Generación de token JWT (requiere autenticación básica).
 *
 * Estructura basada en el ejemplo de Giacomo.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // Inyección del servicio de autenticación
    private final AuthService authService;

    // Inyección del servicio de tokens
    private final TokenService tokenService;

    /**
     * Registro de nuevo usuario
     * Recibe datos de registro, comprueba duplicados y guarda en BD.
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTOResponse> register(
            @Valid @RequestBody UserRegisterDTORequest dto) {
        UserDTOResponse userResponse = authService.register(dto);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    /**
     * Login (validación básica)
     * Comprueba credenciales contra la base de datos.
     * En esta fase todavía no devuelve token, solo confirma autenticación.
     */
    @PostMapping("/login")
    public ResponseEntity<UserDTOResponse> login(
            @Valid @RequestBody UserLoginDTORequest dto) {
        UserDTOResponse userResponse = authService.login(dto);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    /**
     * Generar Token JWT
     * Endpoint protegido por Basic Auth.
     * Una vez autenticado el usuario, se genera un token JWT firmado.
     */
    @PostMapping("/token")
    public ResponseEntity<String> token(Authentication authentication) {
        String jwt = tokenService.generateToken(authentication);
        return ResponseEntity.ok(jwt);
    }
}