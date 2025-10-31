package dev.alejandro.heroquest_backend.hero.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para actualizar las estadísticas del héroe.
 * Se envía desde el frontend (CharacterModal.jsx) cada vez que se cambia un stat.
 * Los nombres de los campos coinciden con los usados en el frontend.
 */
@Getter
@Setter
@NoArgsConstructor
public class HeroUpdateStatsDTO {

    private int vida;              // health
    private int defensa;           // defense
    private int daño;              // attack
    private int movimiento;        // movement
    private int puntosRestantes;   // puntos no asignados del héroe
}