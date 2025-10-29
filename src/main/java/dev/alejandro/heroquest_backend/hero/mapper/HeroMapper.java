package dev.alejandro.heroquest_backend.hero.mapper;

import dev.alejandro.heroquest_backend.hero.dto.HeroDTORequest;
import dev.alejandro.heroquest_backend.hero.dto.HeroDTOResponse;
import dev.alejandro.heroquest_backend.hero.model.Hero;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper con MapStruct para convertir entre Hero y HeroDTO.
 * Evita exponer entidades directamente en la API.
 */
@Mapper(componentModel = "spring")
public interface HeroMapper {

    /**
     * Instancia automática generada por MapStruct.
     * Se puede inyectar como bean de Spring.
     */
    HeroMapper INSTANCE = Mappers.getMapper(HeroMapper.class);

    /**
     * Convierte un DTO de creación en entidad Hero.
     * Se ignora el ID y el usuario porque se asignarán automáticamente.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Hero toHero(HeroDTORequest dto);

    /**
     * Convierte una entidad Hero a DTO de salida.
     * Devuelve todos los datos visibles del héroe sin exponer el usuario.
     */
    @Mapping(source = "movement", target = "movement")
    @Mapping(source = "experiencia", target = "experiencia")
    @Mapping(source = "nivel", target = "nivel")
    HeroDTOResponse toHeroDTOResponse(Hero hero);
}