package dev.alejandro.heroquest_backend.hero.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de salida para enviar información del héroe al frontend.
 * Los nombres se muestran en castellano para la UI.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class HeroDTOResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("clase")
    private String heroClass; // se envía como "clase" en la UI

    @JsonProperty("vida")
    private int health;

    @JsonProperty("ataque") // cambiado de "daño"
    private int attack;

    @JsonProperty("defensa")
    private int defense;

    @JsonProperty("movimiento")
    private int movement;

    @JsonProperty("experiencia")
    private int experiencia;

    @JsonProperty("nivel")
    private int nivel;

    @JsonProperty("puntosRestantes")
    private int puntosRestantes;
}