package dev.alejandro.heroquest_backend.hero.repository;

import dev.alejandro.heroquest_backend.hero.model.Hero;
import dev.alejandro.heroquest_backend.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * HeroRepository
 * Repositorio JPA para la entidad Hero.
 */
@Repository
public interface HeroRepository extends JpaRepository<Hero, Long> {

    // Busca el héroe asociado a un usuario concreto
    Optional<Hero> findByUser(User user);

    // Búsqueda directa por username (evita errores de referencia de entidad)
    Optional<Hero> findByUser_Username(String username);

    // Verifica si un usuario ya tiene héroe
    boolean existsByUser(User user);
}