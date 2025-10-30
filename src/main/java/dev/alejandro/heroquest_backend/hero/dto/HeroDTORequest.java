package dev.alejandro.heroquest_backend.hero.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada simplificado para crear un héroe.
 * Solo se requiere el tipo de héroe (heroClass).
 * Las estadísticas iniciales se asignan automáticamente en el servicio.
 */
@Getter
@Setter
@NoArgsConstructor
public class HeroDTORequest {

    @NotBlank(message = "Hero class is required")
    private String heroClass;
}