package dev.alejandro.heroquest_backend.hero.controller;

import dev.alejandro.heroquest_backend.auth.model.User;
import dev.alejandro.heroquest_backend.auth.repository.UserRepository;
import dev.alejandro.heroquest_backend.hero.dto.HeroDTORequest;
import dev.alejandro.heroquest_backend.hero.dto.HeroDTOResponse;
import dev.alejandro.heroquest_backend.hero.service.HeroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar los héroes de los usuarios autenticados.
 */
@RestController
@RequestMapping("/api/hero")
@RequiredArgsConstructor
public class HeroController {

    private final HeroService heroService;
    private final UserRepository userRepository; // añadimos esto para buscar al usuario en BD


    /**
     * Crear héroe para el usuario autenticado.
     * Un usuario solo puede tener un héroe. Si ya tiene uno, devuelve error.
     */
    @PostMapping
    public ResponseEntity<HeroDTOResponse> createHero(
            @AuthenticationPrincipal Jwt jwt, //  usamos Jwt en lugar de User
            @Valid @RequestBody HeroDTORequest dto) {

        String username = jwt.getSubject(); //  leemos el username desde el token
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        HeroDTOResponse heroResponse = heroService.createHero(user, dto);
        return new ResponseEntity<>(heroResponse, HttpStatus.CREATED);
    }


    /**
     * Obtener el héroe del usuario autenticado.
     */
    @GetMapping
    public ResponseEntity<HeroDTOResponse> getHero(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject(); //  aquí igual
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        HeroDTOResponse heroResponse = heroService.getHeroByUser(user);
        return ResponseEntity.ok(heroResponse);
    }
}