package dev.alejandro.heroquest_backend.inventory.repository;

import dev.alejandro.heroquest_backend.inventory.model.Inventory;
import dev.alejandro.heroquest_backend.hero.model.Hero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Inventory.
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByHero(Hero hero);

    boolean existsByHero(Hero hero);
}
