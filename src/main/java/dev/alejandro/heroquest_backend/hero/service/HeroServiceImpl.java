package dev.alejandro.heroquest_backend.hero.service;

import dev.alejandro.heroquest_backend.auth.model.User;
import dev.alejandro.heroquest_backend.hero.dto.HeroDTORequest;
import dev.alejandro.heroquest_backend.hero.dto.HeroDTOResponse;
import dev.alejandro.heroquest_backend.hero.dto.HeroUpdateStatsDTO;
import dev.alejandro.heroquest_backend.hero.mapper.HeroMapper;
import dev.alejandro.heroquest_backend.hero.model.Hero;
import dev.alejandro.heroquest_backend.hero.repository.HeroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementa la lógica principal de gestión de héroes.
 * - Evita duplicados (solo un héroe por usuario).
 * - Asigna automáticamente las estadísticas base según el tipo de héroe.
 * - Lanza error si el usuario ya tiene un héroe.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class HeroServiceImpl implements HeroService {

    private final HeroRepository heroRepository;
    private final HeroMapper heroMapper;

    @Override
    public HeroDTOResponse createHero(User user, HeroDTORequest dto) {
        // Verificamos que el usuario no tenga ya un héroe asignado
        if (heroRepository.existsByUser(user)) {
            throw new IllegalStateException("This user already has a hero assigned");
        }

        // Creamos la entidad Hero y la asociamos al usuario autenticado
        Hero hero = new Hero();
        hero.setUser(user);
        hero.setHeroClass(dto.getHeroClass());

        // Asignamos estadísticas iniciales según la clase elegida
        switch (dto.getHeroClass().toLowerCase()) {
            case "barbarian":
                hero.setHealth(12);
                hero.setAttack(14);
                hero.setDefense(8);
                hero.setMovement(6);
                hero.setExperiencia(0);
                hero.setNivel(1);
                hero.setPuntosRestantes(15);
                break;

            case "warrior":
                hero.setHealth(10);
                hero.setAttack(10);
                hero.setDefense(12);
                hero.setMovement(5);
                hero.setExperiencia(0);
                hero.setNivel(1);
                hero.setPuntosRestantes(15);
                break;

            default:
                throw new IllegalArgumentException("Unknown hero class: " + dto.getHeroClass());
        }

        // Guardamos en la base de datos y devolvemos la respuesta
        Hero savedHero = heroRepository.save(hero);
        return heroMapper.toHeroDTOResponse(savedHero);
    }

    @Override
    public HeroDTOResponse getHeroByUser(User user) {
        Hero hero = heroRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("This user has no hero assigned"));
        return heroMapper.toHeroDTOResponse(hero);
    }

    /**
     * Actualizar las estadísticas del héroe autenticado.
     * Se llama automáticamente desde el frontend al modificar un stat.
     */
    @Override
    public HeroDTOResponse updateHeroStats(User user, HeroUpdateStatsDTO dto) {
        Hero hero = heroRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("This user has no hero assigned"));

        // Actualizamos los campos con los valores del DTO
        hero.setHealth(dto.getVida());
        hero.setAttack(dto.getDaño());
        hero.setDefense(dto.getDefensa());
        hero.setMovement(dto.getMovimiento());
        hero.setPuntosRestantes(dto.getPuntosRestantes());

        Hero updated = heroRepository.save(hero);
        return heroMapper.toHeroDTOResponse(updated);
    }
}