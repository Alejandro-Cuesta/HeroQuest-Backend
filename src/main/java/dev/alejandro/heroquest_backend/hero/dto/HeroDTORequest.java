package dev.alejandro.heroquest_backend.hero.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para crear un héroe.
 * Solo se permiten los datos básicos del héroe (sin el usuario).
 */
@Getter
@Setter
@NoArgsConstructor
public class HeroDTORequest {

    @NotBlank(message = "Hero class is required")
    private String heroClass;

    @Positive(message = "Health must be positive")
    private int health;

    @Positive(message = "Attack must be positive")
    private int attack;

    @Positive(message = "Defense must be positive")
    private int defense;

    @Positive(message = "Movement must be positive")
    private int movement;

    @Positive(message = "Experiencia must be positive or zero")
    private int experiencia;

    @Positive(message = "Nivel must be positive")
    private int nivel;
}
