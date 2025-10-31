package dev.alejandro.heroquest_backend.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para crear o añadir un ítem al inventario.
 */
@Getter
@Setter
@NoArgsConstructor
public class ItemDTORequest {

    @NotBlank(message = "Item name is required")
    private String name;

    @NotBlank(message = "Item type is required")
    private String type;

    @PositiveOrZero(message = "Attack bonus must be >= 0")
    private int bonusAttack;

    @PositiveOrZero(message = "Defense bonus must be >= 0")
    private int bonusDefense;

    @PositiveOrZero(message = "Health bonus must be >= 0")
    private int bonusHealth;
}