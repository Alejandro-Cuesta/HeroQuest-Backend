package dev.alejandro.heroquest_backend.hero.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de salida para devolver los datos del héroe sin exponer al usuario.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeroDTOResponse {
    private Long id;
    private String heroClass;
    private int health;
    private int attack;
    private int defense;
    private int movement;
    private int experiencia;
    private int nivel;
}