package dev.alejandro.heroquest_backend.hero.service;

import dev.alejandro.heroquest_backend.auth.model.User;
import dev.alejandro.heroquest_backend.hero.dto.HeroDTORequest;
import dev.alejandro.heroquest_backend.hero.dto.HeroDTOResponse;
import dev.alejandro.heroquest_backend.hero.dto.HeroUpdateStatsDTO;

/**
 * Servicio que define las operaciones principales sobre los héroes.
 */
public interface HeroService {

    // Crea un nuevo héroe vinculado al usuario actual
    HeroDTOResponse createHero(User user, HeroDTORequest dto);

    // Obtiene el héroe del usuario actual
    HeroDTOResponse getHeroByUser(User user);

    // Actualiza las estadísticas del héroe autenticado (PUT /api/hero/update-stats)
    HeroDTOResponse updateHeroStats(User user, HeroUpdateStatsDTO dto);
}