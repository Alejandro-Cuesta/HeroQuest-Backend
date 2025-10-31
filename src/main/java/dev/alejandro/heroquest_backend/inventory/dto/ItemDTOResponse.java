package dev.alejandro.heroquest_backend.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de salida para devolver información del ítem sin exponer relaciones.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDTOResponse {
    private Long id;
    private String name;
    private String type;
    private int bonusAttack;
    private int bonusDefense;
    private int bonusHealth;
}

