package dev.alejandro.heroquest_backend.hero.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para actualizar las estadísticas del héroe.
 * Se envía desde el frontend (CharacterModal.jsx) cada vez que se cambia un stat.
 * Los nombres de los campos en JSON están en castellano, pero sin caracteres especiales.
 */
@Getter
@Setter
@NoArgsConstructor
public class HeroUpdateStatsDTO {

    @JsonProperty("vida")
    private int health; // mapeado desde "vida"

    @JsonProperty("defensa")
    private int defense; // mapeado desde "defensa"

    @JsonProperty("ataque")
    private int attack; // mapeado desde "ataque"

    @JsonProperty("movimiento")
    private int movement; // mapeado desde "movimiento"

    private int puntosRestantes; // ya coincide con frontend
}