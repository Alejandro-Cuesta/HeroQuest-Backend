package dev.alejandro.heroquest_backend.hero.controller;

import dev.alejandro.heroquest_backend.auth.model.User;
import dev.alejandro.heroquest_backend.hero.dto.HeroDTORequest;
import dev.alejandro.heroquest_backend.hero.dto.HeroDTOResponse;
import dev.alejandro.heroquest_backend.hero.service.HeroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * HeroController:
 * Controlador REST para gestionar los héroes de los usuarios.
 * - Solo el usuario autenticado puede crear o consultar su héroe.
 */
@RestController
@RequestMapping("/api/hero")
@RequiredArgsConstructor
public class HeroController {

    private final HeroService heroService;

    /**
     * Crear héroe para el usuario autenticado.
     * Un usuario solo puede tener un héroe. Si ya tiene uno, devuelve error.
     */
    @PostMapping
    public ResponseEntity<HeroDTOResponse> createHero(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody HeroDTORequest dto) {
        HeroDTOResponse heroResponse = heroService.createHero(user, dto);
        return new ResponseEntity<>(heroResponse, HttpStatus.CREATED);
    }

    /**
     * Obtener el héroe del usuario autenticado.
     */
    @GetMapping
    public ResponseEntity<HeroDTOResponse> getHero(@AuthenticationPrincipal User user) {
        HeroDTOResponse heroResponse = heroService.getHeroByUser(user);
        return ResponseEntity.ok(heroResponse);
    }
}