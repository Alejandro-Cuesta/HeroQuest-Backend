package dev.alejandro.heroquest_backend.hero.service;

import dev.alejandro.heroquest_backend.auth.model.User;
import dev.alejandro.heroquest_backend.hero.dto.HeroDTORequest;
import dev.alejandro.heroquest_backend.hero.dto.HeroDTOResponse;
import dev.alejandro.heroquest_backend.hero.mapper.HeroMapper;
import dev.alejandro.heroquest_backend.hero.model.Hero;
import dev.alejandro.heroquest_backend.hero.repository.HeroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio HeroService.
 * - Crea un nuevo héroe vinculado a un usuario.
 * - Evita duplicados (solo un héroe por usuario).
 */
@Service
@RequiredArgsConstructor
@Transactional
public class HeroServiceImpl implements HeroService {

    private final HeroRepository heroRepository;
    private final HeroMapper heroMapper;

    @Override
    public HeroDTOResponse createHero(User user, HeroDTORequest dto) {
        // Si ya tiene un héroe, lanzamos error
        if (heroRepository.existsByUser(user)) {
            throw new IllegalStateException("This user already has a hero assigned");
        }

        Hero hero = heroMapper.toHero(dto);
        hero.setUser(user); // vinculamos el héroe al usuario

        Hero savedHero = heroRepository.save(hero);
        return heroMapper.toHeroDTOResponse(savedHero);
    }

    @Override
    public HeroDTOResponse getHeroByUser(User user) {
        Hero hero = heroRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("This user has no hero assigned"));
        return heroMapper.toHeroDTOResponse(hero);
    }
}
