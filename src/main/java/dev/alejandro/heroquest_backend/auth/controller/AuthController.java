package dev.alejandro.heroquest_backend.auth.controller;

import dev.alejandro.heroquest_backend.auth.dto.UserDTOResponse;
import dev.alejandro.heroquest_backend.auth.dto.UserLoginDTORequest;
import dev.alejandro.heroquest_backend.auth.dto.UserRegisterDTORequest;
import dev.alejandro.heroquest_backend.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST que maneja los endpoints públicos de autenticación:
 *  Registro de nuevo usuario (Player)
 *  Inicio de sesión (Login)
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // Inyección del servicio de autenticación
    private final AuthService authService;

    // Endpoint de registro
    @PostMapping("/register")
    public ResponseEntity<UserDTOResponse> register(
            @Valid @RequestBody UserRegisterDTORequest dto) {
        UserDTOResponse userResponse = authService.register(dto);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    // Endpoint de login
    @PostMapping("/login")
    public ResponseEntity<UserDTOResponse> login(
            @Valid @RequestBody UserLoginDTORequest dto) {
        UserDTOResponse userResponse = authService.login(dto);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}

