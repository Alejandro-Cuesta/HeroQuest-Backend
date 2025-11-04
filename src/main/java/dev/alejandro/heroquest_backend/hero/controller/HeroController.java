package dev.alejandro.heroquest_backend.hero.controller;

import dev.alejandro.heroquest_backend.auth.model.User;
import dev.alejandro.heroquest_backend.auth.repository.UserRepository;
import dev.alejandro.heroquest_backend.hero.dto.HeroDTORequest;
import dev.alejandro.heroquest_backend.hero.dto.HeroDTOResponse;
import dev.alejandro.heroquest_backend.hero.dto.HeroUpdateStatsDTO;
import dev.alejandro.heroquest_backend.hero.service.HeroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar los héroes de los usuarios autenticados.
 */
@RestController
@RequestMapping("/api/hero")
@RequiredArgsConstructor
public class HeroController {

    private final HeroService heroService;
    private final UserRepository userRepository;

    /**
     * Crear héroe para el usuario autenticado.
     * Un usuario solo puede tener un héroe. Si ya tiene uno, devuelve error.
     */
    @PostMapping
    public ResponseEntity<HeroDTOResponse> createHero(
            Authentication authentication,
            @Valid @RequestBody HeroDTORequest dto) {

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        HeroDTOResponse heroResponse = heroService.createHero(user, dto);
        return new ResponseEntity<>(heroResponse, HttpStatus.CREATED);
    }

    /**
     * Obtener el héroe del usuario autenticado.
     */
    @GetMapping
    public ResponseEntity<HeroDTOResponse> getHero(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        HeroDTOResponse heroResponse = heroService.getHeroByUser(user);
        return ResponseEntity.ok(heroResponse);
    }

    /**
     * Actualizar estadísticas del héroe autenticado.
     * Endpoint llamado automáticamente desde el frontend cada vez que se modifican los stats.
     * URL: PUT /api/hero/update-stats
     */
    @PutMapping("/update-stats")
    public ResponseEntity<HeroDTOResponse> updateHeroStats(
            Authentication authentication,
            @Valid @RequestBody HeroUpdateStatsDTO dto) {

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        HeroDTOResponse updated = heroService.updateHeroStats(user, dto);
        return ResponseEntity.ok(updated);
    }
}