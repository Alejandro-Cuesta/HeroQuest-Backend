package dev.alejandro.heroquest_backend.hero.mapper;

import dev.alejandro.heroquest_backend.hero.dto.HeroDTOResponse;
import dev.alejandro.heroquest_backend.hero.model.Hero;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre entidad Hero y DTO de respuesta HeroDTOResponse.
 * Aquí aseguramos que los nombres se muestren en castellano en la UI.
 */
@Component
public class HeroMapper {

    public HeroDTOResponse toHeroDTOResponse(Hero hero) {
        if (hero == null) return null;

        return HeroDTOResponse.builder()
                .id(hero.getId())
                .heroClass(hero.getHeroClass())
                .health(hero.getHealth())
                .attack(hero.getAttack())
                .defense(hero.getDefense())
                .movement(hero.getMovement())
                .experiencia(hero.getExperiencia())
                .nivel(hero.getNivel())
                .puntosRestantes(hero.getPuntosRestantes())
                .build();
    }
}