package dev.alejandro.heroquest_backend.inventory.repository;

import dev.alejandro.heroquest_backend.inventory.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad Item.
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
